package project.bridgetek.com.applib.main.bean;

/**
 * Created by bridge on 18-6-25.
 */

public class ResultFileInfo {
    String FileType;
    String FileName;
    String FileDataForBase64String;
    String CheckItem_ID;
    String Exception_ID;

    public ResultFileInfo() {
    }

    public ResultFileInfo(String fileType, String fileName, String fileDataForBase64String, String checkItem_ID, String exception_ID) {
        FileType = fileType;
        FileName = fileName;
        FileDataForBase64String = fileDataForBase64String;
        CheckItem_ID = checkItem_ID;
        Exception_ID = exception_ID;
    }

    public ResultFileInfo(String fileType, String fileName, String fileDataForBase64String, String checkItem_ID) {
        FileType = fileType;
        FileName = fileName;
        FileDataForBase64String = fileDataForBase64String;
        CheckItem_ID = checkItem_ID;
    }

    public String getCheckItem_ID() {
        return CheckItem_ID;
    }

    public void setCheckItem_ID(String checkItem_ID) {
        CheckItem_ID = checkItem_ID;
    }

    public String getFileType() {
        return FileType;
    }

    public void setFileType(String fileType) {
        FileType = fileType;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileDataForBase64String() {
        return FileDataForBase64String;
    }

    public void setFileDataForBase64String(String fileDataForBase64String) {
        FileDataForBase64String = fileDataForBase64String;
    }

    public String getException_ID() {
        return Exception_ID;
    }

    public void setException_ID(String exception_ID) {
        Exception_ID = exception_ID;
    }
}
