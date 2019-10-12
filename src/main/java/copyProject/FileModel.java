package copyProject;

/**
 * Created by ma peiliang
 * Create Date: 2019/1/9 11:48
 * Description: ${DESCRIPTION}
 */
public class FileModel {

    private String sourcedirectoryPath;

    private String directoryPath ;


    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public String getSourcedirectoryPath() {
        return sourcedirectoryPath;
    }

    public void setSourcedirectoryPath(String sourcedirectoryPath) {
        this.sourcedirectoryPath = sourcedirectoryPath;
    }
}
