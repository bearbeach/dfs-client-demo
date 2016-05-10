<%@ page import="java.util.List" %>
<%@ page import="com.baofoo.dfs.client.model.DfsNode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Baofoo Fast DFS Server</title>
    <style type="text/css">
        table.imagetable {
            font-family: verdana,arial,sans-serif;
            font-size:11px;
            color:#333333;
            border-width: 1px;
            border-color: #999999;
            border-collapse: collapse;
        }
        table.imagetable th {
            background:#b5cfd2;
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #999999;
        }
        table.imagetable td {
            background:#dcddc0 ;
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #999999;
        }

    </style>
</head>
<body>
    <p>宝付FastDFS 服务</p>

    <table class="imagetable">
        <tr>
            <th>zookeeper node path</th>
            <th>Zookeeper node value </th>
        </tr>
        <%
        List<DfsNode> servers = (List<DfsNode>) request.getAttribute("servers");
        for (DfsNode server:servers){
        %>
        <tr>
            <td><%= server.getServerNode() %></td>
            <td><%= server.getNodeValue() %></td>
        </tr>
        <%
        }
    %>
    </table>

<script>
    window.setInterval(reShow,1000 * 10);

    function reShow(){
        location.reload();
    }
</script>
</body>
</html>
