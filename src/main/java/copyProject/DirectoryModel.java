package copyProject;

import java.util.List;

/**
 * Created by ma peiliang
 * Create Date: 2019/1/9 11:47
 * Description: ${DESCRIPTION}
 */
public class DirectoryModel {

    private String path ;

    private String catalogName;

    /**
     * 目录内第一层子目录
     */
    private List<DirectoryModel> catalogModelList;

    /**
     * 目录内第一层子文件
     */
    private List<FileModel> fileModelList;

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public List<DirectoryModel> getCatalogModelList() {
        return catalogModelList;
    }

    public void setCatalogModelList(List<DirectoryModel> catalogModelList) {
        this.catalogModelList = catalogModelList;
    }

    public List<FileModel> getFileModelList() {
        return fileModelList;
    }

    public void setFileModelList(List<FileModel> fileModelList) {
        this.fileModelList = fileModelList;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
