<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>发件人详情</title>
    <style>
        .content {
            padding: 15px 100px;
            min-width: 920px;
        }

        .email-title {
            margin: 0 auto;
            text-align: center;
            font-size: 34px;
        }

        .email-title span {
            background: url("data:image/png;base64,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") no-repeat left center;
            background-size: 50px;
            padding-left: 60px;
        }

        .sub-form {
            margin: 0 auto;
            margin-top: 30px;
        }

        .sub-form div.item {
            width: 100%;
            margin-bottom: 20px;
        }

        .row-ul {
            margin-top: 20px;
        }

        .row-ul div {
            float: left;
            margin-right: 20px;
            list-style: none;
        }

        .row-ul div input {
            height: 30px;
            width: 200px;
        }

        .clearfix:after, .clearfix:before {
            content: "";
            display: table;
        }

        .clearfix:after {
            clear: both;
        }

        .clearfix {
            *zoom: 1;
        }

        input[name='title'] {
            height: 30px;
            width: 50%;
        }

        .mustbe {
            font-size: 12px;
            color: red;
            padding-left: 10px;
        }

        textarea[name="content"] {
            height: 300px;
            width: 100%;
            resize: none;
            border: 1px solid #037503;
        }

        .upinput {
            height: 30px;
            width: 200px;
            margin-right: 15px;
            margin-top: 20px;
        }

        .upbtn {
            padding: 2px 5px;
            font-size: 14px;
            background: #037503;
            color: #fff;
            cursor: pointer;
        }

        .upfile {
            position: absolute;
            left: 268px;
            top: 21px;
            opacity: 0;
            cursor: pointer;
        }

        .pr {
            position: relative;
        }

        input[type="submit"] {
            padding: 4px 35px;
            cursor: pointer;
        }

        .tr {
            text-align: right;
        }


    </style>
    <script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#but1").click(function () {
                window.location.href = "${pageContext.servletContext.contextPath}/email/show?pageIndex=1";
            })
        })
    </script>
</head>
<body>
<div class="content">
    <h4 class="email-title"><span>局长收信箱</span></h4>
    <div class="item"> 标题： <input type="text" name="title" value="${emailInfo.title}"></div>

    <div class="item"> 内容： <textarea type="text" name="content">${emailInfo.content}</textarea></div>
    <div class="item pr"> 附件： <a
            href="${pageContext.servletContext.contextPath}/email/downLoad?fileName=${emailInfo.document}">${emailInfo.document}</a><span
            class="mustbe">点击可下载附件</span></div>

    <div class="item">
        <div class="row-ul clearfix">
            <div>发件人: <input type="text" name="sender" value="${emailInfo.sender}"></div>
            <div>单位: <input type="text" name="company" value="${emailInfo.company}"></div>
            <div>联系方式: <input type="text" name="phoneNum" value="${emailInfo.phoneNum}"></div>
        </div>
    </div>
    <div class="item tr"><input type="button" value="返回" id="but1"></div>

</div>
</body>
</html>
