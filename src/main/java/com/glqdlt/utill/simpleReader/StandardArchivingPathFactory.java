package com.glqdlt.utill.simpleReader;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @author Jhun
 * 2019-03-27
 */
public class StandardArchivingPathFactory implements ArchivingPathFactory{
    private AuthenticationStore authenticationStore;

    public StandardArchivingPathFactory(AuthenticationStore authenticationStore) {
        this.authenticationStore = authenticationStore;
    }

    @Override
    public String getArchivePath(String filePrefix) {
        final String excel = ".xslx";
        LocalDateTime aa = LocalDateTime.now();
        String date = aa.format(DateTimeFormatter.ofPattern("YYYY_MM_dd"));
        String time = aa.format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss"));
        String s = File.separator;
        File root = File.listRoots()[0];

        String adminId = authenticationStore.getAuthencatedId();

        return root.getAbsolutePath() + s + "opt" + s + "www" + s + "mpoker_cms_v2" + s + "archive" + s + date + s + adminId + "_" + filePrefix + "_" + time + excel;
    }
}
