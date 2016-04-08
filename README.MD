# DFS-CORE
* FastDFS封装层


## DFS-CLIENT

Maven 引入DFS-CLIENT

	<dependency>
        <groupId>com.baofoo</groupId>
        <artifactId>dfs-client</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>


Spring 配置 命名空间引入

	xmlns:baofoo="http://www.baofoo.com/schema/tags"
	http://www.baofoo.com/schema/tags
	http://www.baofoo.com/schema/tags/dfs-1.0.0.xsd

SPring 配置

	<!-- host 为dfs-server 服务地址，port为服务端口，trakers 为DFS tracker server地址，多个用英文逗号隔开 -->
	<baofoo:dfs host="127.0.0.1" port="9090" trackers="10.0.21.56:22122,10.0.21.57:22122"/>

Java调用 提供上传、下载、删除等方法

	import com.baofoo.dfs.client.DfsClient

	DfsClient.upload("F:\\嘉兴拓展.zip","orgCode","9嘉兴拓展.zip","文件分组","文件日期");


    DfsClient.download("M00/00/00/CgAVOVZYFPuAFEQNE8UwY4CYfGg986.zip","D:\\aaa.zip");

    DfsClient.download("fileName","123","2015-11-27","D:\\test.sql");

    DfsClient.download(7L,"D:\\");

    DfsClient.delete(6L);
	

	
