package com.dms.guyiyao.mapper;

public interface LogMapper {
    int loginFailException(String url,String user,String ip,String exception,String param,String type,String time,String server);

    void CommonException(String url, String user, String ip, String exception, String param, String type,String time,String server);
}
