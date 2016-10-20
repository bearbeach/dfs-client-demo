#  DFS-CORE
## 项目介绍

DFS-CORE 底层是基于FastDFS开发出来的。首先还是来简单介绍一下FastDFS。

### FastDFS

FastDFS是一个开源的轻量级分布式文件系统，由跟踪服务器（tracker server）、存储服务器（storage server）和客户端（client）三个部分组成，主要解决了海量数据存储问题，特别适合以中小文件（建议范围：4KB < file_size <500MB）为载体的在线服务。


FastDFS服务端有三个角色：跟踪服务器（tracker server）、存储服务器（storage server）和客户端（client）。

![FastDFS 原理图](http://images.51cto.com/files/uploadimg/20121009/1647200.png)


* Storage server  

简称storage，以组（卷，group或volume）为单位组织，一个group内包含多台storage机器，数据互为备份，存储空间以group内容量最小的storage为准，所以建议group内的多个storage尽量配置相同，以免造成存储空间的浪费。

以group为单位组织存储能方便的进行应用隔离、负载均衡、副本数定制（group内storage server数量即为该group的副本数），比如将不同应用数据存到不同的group就能隔离应用数据，同时还可根据应用的访问特性来将应用分配到不同的group来做负载均衡；缺点是group的容量受单机存储容量的限制，同时当group内有机器坏掉时，数据恢复只能依赖group内地其他机器，使得恢复时间会很长。

group内每个storage的存储依赖于本地文件系统，storage可配置多个数据存储目录，比如有10块磁盘，分别挂载在/data/disk1-/data/disk10，则可将这10个目录都配置为storage的数据存储目录。

storage接受到写文件请求时，会根据配置好的规则（后面会介绍），选择其中一个存储目录来存储文件。为了避免单个目录下的文件数太多，在storage第一次启动时，会在每个数据存储目录里创建2级子目录，每级256个，总共65536个文件，新写的文件会以hash的方式被路由到其中某个子目录下，然后将文件数据直接作为一个本地文件存储到该目录中。


* Tracker server

Tracker是FastDFS的协调者，负责管理所有的storage server和group，每个storage在启动后会连接Tracker，告知自己所属的group等信息，并保持周期性的心跳，tracker根据storage的心跳信息，建立group==>[storage server list]的映射表。

Tracker需要管理的元信息很少，会全部存储在内存中；另外tracker上的元信息都是由storage汇报的信息生成的，本身不需要持久化任何数据，这样使得tracker非常容易扩展，直接增加tracker机器即可扩展为tracker cluster来服务，cluster里每个tracker之间是完全对等的，所有的tracker都接受stroage的心跳信息，生成元数据信息来提供读写服务。

* Client

Client 即每一个使用FastDFS的业务系统。

来看一个FastDFS 文件上传的原理图，看看FastDFS 是如何工作的。
![文件上传](http://haystack.u.qiniudn.com/upload_file.jpg)

再来看个文件下载流程。
![文件下载](http://haystack.u.qiniudn.com/download_file.jpg)

看完这两个图，你大概能理解Tracker 和 Storage 在FastDFS所扮演的角色以及具体是如何工作的了。 :-D


## DFS-CORE

DFS-CORE 中主要包含了两个角色 server，client。


### dfs-server
	
我们封装这一层主要是为了管理和约束我们上传的文件信息。Server记录并存储着所有其它业务系统中存放在宝付分布式文件系统中的文件信息，主要包括文件的存放ID，存放目录，上传日期，文件类型等信息（FastDFS本身并不提供对文件信息的保存，它只关心文件的存放）。同时Server端又约束了文件上传的规范，如：文件的类型，名称，分组，有效期等。


### dfs-client
主要封装 FastDFS 的 Java API 及 与 **Server** 同步文件记录信息。如业务系统上传一个文件的流程是：1、client 先把上传的文件信息同步到server，2、server 返回保存结果（重复校验），client 将文件上传至FastDfs集群，3、FastDfs 返回上传结果信息，4、client 同步更新文件信息到server，5、server返回文件信息更新结果。通常这些流程需要业务系统全部完成，在封装了Client 和Server后，业务系统只需要简单的调用Client 的相关接口即可。

结合下图了解dfs-core 是如何进行分布式文件处理的。

![DFS-CORE](https://raw.githubusercontent.com/chinazhen/images/master/dfs-core.jpg)


## 最新版本

- 2.0.0
- jdk 1.6

	1.client 端修改配置方式，增加httpServer配置项（dfs 对外暴漏http服务负载地址）。


## 使用帮助

该版本主要变化为：服务架构改为分布式架构，支持服务端分布式部署，支持动态增加服务端。配置稍微变化，客户端不在关注服务端配置，采用zookeeper服务注册及监听服务进行集群管理。


### Maven 引入DFS-CLIENT

	<dependency>
        <groupId>com.baofoo</groupId>
        <artifactId>dfs-client</artifactId>
        <version>5.0.0</version>
    </dependency>


### Spring 配置 命名空间引入

	xmlns:baofoo="http://www.baofoo.com/schema/tags"
	http://www.baofoo.com/schema/tags
	http://www.baofoo.com/schema/tags/dfs-1.2.0.xsd

### Spring 配置

	<!-- zookeeper 服务注册地址，trakers 为DFS tracker server地址，多个用英文逗号隔开，trackerHttpPort 为http服务端口，httpServer为http对外暴漏的负载地址，secretKey为生成下载地址的加密token，不同的fastDFS集群配置不尽相同  -->
	<baofoo:dfs zookeeper="10.0.20.175:2181" trackers="10.0.21.130:22122" trackerHttpPort="8080" httpServer="10.0.21.130" secretKey="1qazXsw28080"/>

### Java Example

    demo地址:`com.baofoo.dfs.demo.DfsTest`

    .......
    
    /**
     * 上传文件
     */
    @Test
    public void upload() {
        InsertReqDTO insertReqDTO = new InsertReqDTO();
        insertReqDTO.setFilePath("/Users/weiwei/Downloads/1.txt");
        insertReqDTO.setOrgCode("orgCode");
        insertReqDTO.setFileGroup(FileGroup.FINANCE.getCode());
        insertReqDTO.setFileName("1.txt");
        insertReqDTO.setFileDate("2016-08-24");

        DfsClient.upload(insertReqDTO);
    }
    
    .....