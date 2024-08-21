package com.arms.egovframework.javaservice.esframework.filter;

import com.arms.egovframework.javaservice.esframework.Filter;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import java.time.LocalDate;

public class RangeQueryFilter extends Filter<RangeQueryBuilder> {

    private RangeQueryBuilder rangeQueryBuilder;

    public RangeQueryFilter(String name, Object from, Object to, String flag) {
        if (name != null && flag != null) {
            if("lt".equals(flag)) {
                if(to != null) {
                    this.rangeQueryBuilder = QueryBuilders.rangeQuery(name).lt(to);
                }
            } else if ("lte".equals(flag)) {
                if(to != null) {
                    this.rangeQueryBuilder = QueryBuilders.rangeQuery(name).lte(to);
                }
            } else if ("gt".equals(flag)) {
                if(from != null) {
                    this.rangeQueryBuilder = QueryBuilders.rangeQuery(name).gt(from);
                }
            } else if ("gte".equals(flag)) {
                if(from != null) {
                    this.rangeQueryBuilder = QueryBuilders.rangeQuery(name).gte(from);
                }
            } else if ("fromto".equals(flag)) {
                if(from != null && to != null) {
                    this.rangeQueryBuilder = QueryBuilders.rangeQuery(name).from(from).to(to);
                }
            }
        }
    }

    private RangeQueryFilter(String name){
        this.rangeQueryBuilder = QueryBuilders.rangeQuery(name);
    }

    public static RangeQueryFilter of(String name){
        return new RangeQueryFilter(name);
    }


    public RangeQueryFilter lt(LocalDate lt){
        if(lt!=null){
            this.rangeQueryBuilder.lt(lt);
        }
        return this;
    }

    public RangeQueryFilter lt(String lt){
        if(lt!=null){
            this.rangeQueryBuilder.lt(lt);
        }
        return this;
    }

    public RangeQueryFilter lte(LocalDate lte){
        if(lte!=null){
            this.rangeQueryBuilder.lte(lte);
        }
        return this;
    }

    public RangeQueryFilter lte(String lte){
        if(lte!=null){
            this.rangeQueryBuilder.lte(lte);
        }
        return this;
    }

    public RangeQueryFilter gt(LocalDate gt){
        if(gt!=null){
            this.rangeQueryBuilder.gt(gt);
        }
        return this;
    }

    public RangeQueryFilter gt(String gt){
        if(gt!=null){
            this.rangeQueryBuilder.gt(gt);
        }
        return this;
    }

    public RangeQueryFilter gte(LocalDate gte){
        if(gte!=null){
            this.rangeQueryBuilder.gte(gte);
        }
        return this;
    }

    public RangeQueryFilter gte(String gte){
        if(gte!=null){
            this.rangeQueryBuilder.gte(gte);
        }
        return this;
    }

    public RangeQueryFilter from(LocalDate from){
        if(from!=null){
            this.rangeQueryBuilder.from(from);
        }
        return this;
    }

    public RangeQueryFilter from(String from){
        if(from!=null){
            this.rangeQueryBuilder.from(from);
        }
        if(from==null){
            this.rangeQueryBuilder = null;
        }

        return this;
    }

    public RangeQueryFilter to(LocalDate to){
        if(to!=null){
            this.rangeQueryBuilder.to(to);
        }
        return this;
    }

    public RangeQueryFilter to(String to){
        if(to!=null){
            this.rangeQueryBuilder.to(to);
        }
        if(to==null){
            this.rangeQueryBuilder = null;
        }
        return this;
    }

    @Override
    public AbstractQueryBuilder<RangeQueryBuilder> abstractQueryBuilder() {
        return rangeQueryBuilder;
    }
}
