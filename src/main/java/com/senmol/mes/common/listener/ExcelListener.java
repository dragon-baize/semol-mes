package com.senmol.mes.common.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.senmol.mes.produce.service.MaterialService;
import com.senmol.mes.warehouse.service.StorageService;

import java.util.ArrayList;
import java.util.List;

/**
 * 有个很重要的点 ExcelListener 不能被spring管理，要每次读取excel都要new，然后里面用到spring可以构造方法传进去
 *
 * @author Administrator
 */
public class ExcelListener implements ReadListener<Object> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private final static int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<Object> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    /**
     * 未导入成功的数据
     */
    private final List<Object> failList = new ArrayList<>();
    /**
     * 传入的service
     */
    private final Object object;

    /**
     * 每次创建Listener的时候需要把spring管理的类传进来
     */
    public ExcelListener(Object object) {
        this.object = object;
    }

    public List<Object> getFailList() {
        return failList;
    }

    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(Object data, AnalysisContext context) {
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        if (!cachedDataList.isEmpty()) {
            saveData();
        }
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        if (object instanceof StorageService) {
            StorageService storageService = (StorageService) object;
            failList.addAll(storageService.insertByExcel(cachedDataList));
        } else if (object instanceof MaterialService) {
            MaterialService materialService = (MaterialService) object;
            failList.addAll(materialService.insertByExcel(cachedDataList));
        }
    }

}
