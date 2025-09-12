package com.senmol.mes.workflow.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.FlowCache;
import com.senmol.mes.plan.entity.ProduceEntity;
import com.senmol.mes.plan.service.ProduceService;
import com.senmol.mes.produce.entity.BomEntity;
import com.senmol.mes.produce.service.BomService;
import com.senmol.mes.produce.vo.BomVo;
import com.senmol.mes.produce.vo.WorkmanshipVo;
import com.senmol.mes.workflow.entity.FlowRecord;
import com.senmol.mes.workflow.service.FlowRecordService;
import com.senmol.mes.workorder.service.WorkOrderMaterialService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Configuration
public class FlowAsyncUtil {

    @Resource
    private RedisService redisService;
    @Resource
    private FlowRecordService flowRecordService;
    @Resource
    private ProduceService produceService;
    @Resource
    private BomService bomService;
    @Resource
    private WorkOrderMaterialService workOrderMaterialService;
    private static final String PLAN_PRODUCE = "plan_produce";
    private static final String PRODUCE_BOM = "produce_bom";
    private static final String PRODUCE_WORKMANSHIP = "produce_workmanship";

    /**
     * 新增流程
     */
    @Async
    public void addFlowRecord(String tableName, String table, Object object) {
        Object id = ReflectUtil.getFieldValue(object, "id");
        Object code = ReflectUtil.getFieldValue(object, "code");
        Object orderNo = ReflectUtil.getFieldValue(object, "orderNo");
        Object title = ReflectUtil.getFieldValue(object, "title");
        Object createUser = ReflectUtil.getFieldValue(object, "createUser");

        FlowRecord record = new FlowRecord();
        record.setItemId(Long.valueOf(id.toString()));
        record.setTitle(table);

        // 物料清单取产品code
        if (object instanceof BomEntity) {
            code = ReflectUtil.getFieldValue(object, "productCode");
        }

        record.setItemCode(ObjectUtil.isNull(code) ? ObjectUtil.defaultIfNull(orderNo, "").toString() :
                code.toString());
        record.setItemTitle(ObjectUtil.defaultIfNull(title, "").toString());
        record.setTableName(tableName);
        record.setCreateTime(LocalDateTime.now());
        record.setCreateUser(Long.valueOf(createUser.toString()));
        this.flowRecordService.insertFlowRecord(record);

        this.dealCache(100, record);
    }

    /**
     * 删除流程
     */
    @Async
    public void delFlow(Object id) {
        this.flowRecordService.deleteFlowRecord(Long.parseLong(id.toString()));
    }

    /**
     * 处理审批结果
     */
    @Async
    public void dealFlowStatus(int flag, Long id, Long userId) {
        FlowRecord record = this.flowRecordService.getById(id);
        // 审核不通过
        int status;
        if (flag == 2) {
            status = 200;
        } else {
            status = 0;

            if (PLAN_PRODUCE.equals(record.getTableName())) {
                ProduceEntity produce = this.produceService.getById(record.getItemId());
                // 生产计划推到工单
                if (ObjectUtil.isNotNull(produce)) {

                    // 生产计划状态为待生产-0
                    this.produceService.lambdaUpdate()
                            .set(ProduceEntity::getStatus, status)
                            .set(ProduceEntity::getUpdateTime, LocalDateTime.now())
                            .set(ProduceEntity::getUpdateUser, userId)
                            .eq(ProduceEntity::getId, produce.getId())
                            .update();
                }
            }
        }

        this.flowRecordService.updateStatus(record.getTableName(), status, record.getItemId());
        this.dealCache(status, record);
    }

    /**
     * 处理生产计划
     */
    @Async
    public void dealProduce(FlowCache flowCache, Object param) {
        boolean add = flowCache.isAdd();
        Class<?> aClass;
        try {
            aClass = Class.forName(flowCache.entity());
            Object object = Convert.convert(aClass, param);
            ProduceEntity produce = Convert.convert(ProduceEntity.class, object);

//            if (!add) {
//                // 删除任务单、任务单物料、任务单不良模式
//                WorkOrderEntity workOrder =
//                        this.workOrderService.lambdaQuery().eq(WorkOrderEntity::getPlanId, produce.getId()).one();
//                if (ObjectUtil.isNotNull(workOrder)) {
//                    this.workOrderService.lambdaUpdate().eq(WorkOrderEntity::getPlanId, produce.getId()).remove();
//                    this.workOrderMaterialService.lambdaUpdate().eq(WorkOrderMaterial::getPid, workOrder.getId()).remove();
//                }
//            }
        } catch (ClassNotFoundException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 开启流程
     */
    @Async
    public void openFlow(Object hGet, FlowCache flowCache, Object param) {
        Class<?> aClass;
        try {
            aClass = Class.forName(flowCache.entity());
            Object object = Convert.convert(aClass, param);
            Object mrp = ReflectUtil.getFieldValue(object, "mrp");
            // MRP创建的跳过
            if (ObjectUtil.isNotNull(mrp) && Integer.parseInt(mrp.toString()) == 0) {
                return;
            }

            Object id = ReflectUtil.getFieldValue(object, "id");

            // 变更表状态，100-审核中
            this.flowRecordService.updateStatus(hGet, 100, id);

            // 新增流程信息
            this.addFlowRecord(hGet.toString(), flowCache.table(), object);
        } catch (ClassNotFoundException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 处理缓存的状态
     */
    private void dealCache(Integer status, FlowRecord record) {
        if (PRODUCE_WORKMANSHIP.equals(record.getTableName())) {
            Object object = this.redisService.get(RedisKeyEnum.PRODUCE_WORKMANSHIP.getKey() + record.getItemId());
            WorkmanshipVo workmanshipVo = Convert.convert(WorkmanshipVo.class, object);
            workmanshipVo.setStatus(status);
            this.redisService.set(RedisKeyEnum.PRODUCE_WORKMANSHIP.getKey() + record.getItemId(), workmanshipVo,
                    RedisKeyEnum.PRODUCE_WORKMANSHIP.getTimeout());
        }

        if (PRODUCE_BOM.equals(record.getTableName())) {
            BomEntity bom = this.bomService.getById(record.getItemId());
            Object object = this.redisService.get(RedisKeyEnum.PRODUCE_BOM.getKey() + bom.getProductId());
            BomVo bomVo = Convert.convert(BomVo.class, object);
            bomVo.setStatus(status);
            this.redisService.set(RedisKeyEnum.PRODUCE_BOM.getKey() + bom.getProductId(), bomVo);
        }
    }

}
