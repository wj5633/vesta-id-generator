package com.wj5633.vesta.service;

import com.wj5633.vesta.bean.Id;

import java.util.Date;

/**
 * Created at 2019/7/16 13:33.
 *
 * @author wangjie
 * @version 1.0.0
 */

public interface IdService {

    long genId();

    Id expId(long id);

    long makeId(long time, long seq);

    long makeId(long time, long seq, long machine);

    long makeId(long genMethod, long time, long seq, long machine);

    long makeId(long type, long genMethod, long time, long seq, long machine);

    long makeId(long version, long type, long genMethod, long time, long seq, long machine);

    Date transTime(long time);
}
