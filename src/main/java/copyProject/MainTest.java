package copyProject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ma peiliang
 * Create Date: 2019/1/9 11:44
 * Description: ${DESCRIPTION}
 */
public class MainTest {

    private static String s1 = "com.biyao.";
    private static String s2 = ".biyao.com";

    public static void main(String[] args){
        String path = "D:\\code_responsity\\activity";
        String name = "activity";

        DirectoryModel projectModel = new DirectoryModel();
        projectModel.setPath(path);
        projectModel.setCatalogName(name);
        projectModel =   parseSourceDirectory(projectModel);

        projectModel = replaceDirectoryName(projectModel,"activity","newName");

        initNewProject(projectModel,"activity","newName");
    }

    /**
     * 解析目录
     * @return 解析后项目对象
     */
    private static DirectoryModel parseSourceDirectory(DirectoryModel directoryModel ){

        File file =  new File(directoryModel.getPath());
        String[] list = file.list();
        List<DirectoryModel> directoryModelList = new ArrayList<>();
        List<FileModel> fileModelList = new ArrayList<>();
        if(list != null && list.length > 0){
            for (String aList : list) {
                File temp = new File(directoryModel.getPath() + "\\" + aList);
                if (temp.isDirectory() && !aList.equals(".git")) {
                    DirectoryModel item = new DirectoryModel();
                    item.setCatalogName(aList);
                    item.setPath(directoryModel.getPath() + "\\" + aList);
                    directoryModelList.add(item);
                }
                if (temp.isFile()) {
                    FileModel item = new FileModel();
                    item.setFileName(aList);
                    item.setSourcedirectoryPath(directoryModel.getPath()+"\\" + aList);
                    fileModelList.add(item);
                }
            }
        }

        directoryModel.setCatalogModelList(directoryModelList);
        directoryModel.setFileModelList(fileModelList);
        for(DirectoryModel item : directoryModelList){
            parseSourceDirectory(item);
        }
        return directoryModel;

    }

    /**
     * 替换目录名称
     * @return 替换后项目对象
     */
    private static DirectoryModel replaceDirectoryName(DirectoryModel directoryModel,String oldName,String newName){

        directoryModel.setPath(directoryModel.getPath().replaceAll(oldName,newName));
        directoryModel.setCatalogName(directoryModel.getCatalogName().equals(oldName) ? newName : directoryModel.getCatalogName());

        for(DirectoryModel item : directoryModel.getCatalogModelList()){
            replaceDirectoryName(item,oldName,newName);
        }

        for(FileModel item : directoryModel.getFileModelList()){
            item.setDirectoryPath(item.getSourcedirectoryPath().replaceAll(oldName,newName));
            item.setDirectoryPath(item.getDirectoryPath().replaceAll(oldName+"-",newName+"-"));
        }
        return directoryModel;

    }


    /**
     * 构建新项目
     * @return
     */
    private static void initNewProject(DirectoryModel directoryModel,String source,String targert){
        File file =  new File(directoryModel.getPath());

        if(!file.exists()){
            file.mkdir();
        }
        for(DirectoryModel item : directoryModel.getCatalogModelList()){
            initNewProject(item,source,targert);
        }

        for(FileModel item : directoryModel.getFileModelList()){
            initNewFile(item,source,targert);
        }

    }

    private static void initNewFile(FileModel item,String source,String targert){
        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(new File(item.getSourcedirectoryPath()))));//数据流读取文件

            StringBuffer strBuffer = new StringBuffer();
            String temp;
            while((temp = bufReader.readLine()) != null) {
                temp =  temp.replaceAll(s1+source,s1+targert);
                temp =  temp.replaceAll(source+s2,targert+s2);

                temp = temp.replaceAll(source+"-",targert+"-");
                temp = temp.replaceAll("<artifactId>"+source+"</artifactId>","<artifactId>"+targert+"</artifactId>");
                temp = temp.replaceAll(source+"\\.iml",targert+"\\.iml");

                strBuffer.append(temp);
                strBuffer.append(System.getProperty("line.separator"));//行与行之间的分割
            }
            bufReader.close();
            PrintWriter printWriter = new PrintWriter(item.getDirectoryPath());//替换后输出的文件位置
            printWriter.write(strBuffer.toString().toCharArray());
            printWriter.flush();
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
