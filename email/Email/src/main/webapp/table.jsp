<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>展示邮件</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript">
        function remove(sid) {
            var b = confirm("确定删除吗？");
            if (b == true) {
                $.post("${pageContext.servletContext.contextPath}/email/remove", {sid: sid}, function (data) {
                    if (data == "ok") {
                        window.location.href = "${pageContext.servletContext.contextPath}/email/show";
                    }
                });
            }
        }
    </script>
</head>
<body>
<table border="1" class="table table-hover table-striped table-bordered">
    <tr>
        <td>序号</td>
        <td>邮件标题</td>
        <td>附件</td>
        <td>发件人</td>
        <td>收件时间</td>
        <td>发件人单位</td>
        <td>联系方式</td>
        <td>功能1</td>
        <td>功能2</td>
        <td>状态</td>
    </tr>
    <c:forEach var="e" items="${map.emailInfos}" varStatus="seq">

        <tr>
            <td>${seq.index+1}</td>
            <td>${e.title}</td>
            <td>
                <a href="${pageContext.servletContext.contextPath}/email/downLoad?fileName=${e.document}">${e.document}</a>
            </td>
            <td>${e.sender}</td>
            <td><fmt:formatDate value="${e.acceptTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
            <td>${e.company}</td>
            <td>${e.phoneNum}</td>
            <td><a href="${pageContext.servletContext.contextPath}/email/detail?sid=${e.eid}">详情</a></td>
            <td><input type="button" value="删除" onclick="remove('${e.eid}')"></td>
            <td>
                <c:if test="${e.look_status == true}">
                    <a href="${pageContext.servletContext.contextPath}/email/updateStatus?eid=${e.eid}&look_status=false">已查阅</a>
                </c:if>
                <c:if test="${e.look_status == false}">
                    <a href="${pageContext.servletContext.contextPath}/email/updateStatus?eid=${e.eid}&look_status=true">未读</a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>
<div class="row">
    <div class="col-xs-4">
        <li>
            当前页数为：<input id="pageindext" type="button" readonly="readonly" value="${map.pageIndex}">
        </li>
    </div>
    <input type="hidden" value="1" id="pageIndexx">
    <div class="col-xs-4">
        <nav aria-label="Page navigation">
            <form action="${pageContext.servletContext.contextPath}/email/show" method="get">
                跳转到第<input type="text" name="pageIndex">页
                <input type="submit" value="跳转">
            </form>
        </nav>
    </div>
    <div class="col-xs-4">
        <li>
            共<input id="pagecountt" type="button" readonly="readonly" value="${map.pageCount}">页
        </li>
    </div>
</div>
</body>


