package com.dms.guyiyao.service;

import java.io.InputStream;

public interface FtpService {
    String FtpUpload(String fileName,
                     String dirPath, InputStream inputStream);
}
