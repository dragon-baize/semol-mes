package com.senmol.mes.common.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.OrderByElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PageInterceptor extends PaginationInnerInterceptor {

    private DbType dbType;

    public PageInterceptor(DbType dbType) {
        super(dbType);
        this.dbType = dbType;
    }

    @Override
    protected void handlerOverflow(IPage<?> page) {
        long current = page.getCurrent();
        page.setCurrent(current - 1);
    }

    @Override
    protected List<OrderByElement> addOrderByElements(List<OrderItem> orderList, List<OrderByElement> orderByElements) {
        List<OrderByElement> additionalOrderBy = orderList.stream()
                .filter(item -> StringUtils.isNotBlank(item.getColumn()))
                .map(item -> {
                    OrderByElement element = new OrderByElement();
                    element.setExpression(new Column(StrUtil.toUnderlineCase(item.getColumn())));
                    element.setAsc(item.isAsc());
                    element.setAscDescPresent(true);
                    return element;
                }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(orderByElements)) {
            return additionalOrderBy;
        }
        // github pull/3550 优化排序，比如：默认 order by id 前端传了name排序，设置为 order by name,id
        additionalOrderBy.addAll(orderByElements);
        return additionalOrderBy;
    }
}
