<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>蓝源Eloan-P2P平台(系统管理平台)</title>
<#include "../common/header.ftl"/>
    <script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
    <script type="text/javascript" src="/js/plugins/jquery-validation/jquery.validate.js"></script>
    <script type="text/javascript" src="/js/plugins/jquery.twbsPagination.min.js"></script>

    <script type="text/javascript">
        $(function () {
            $(function () {
                $("#pagination").twbsPagination({
                    totalPages:${pageResult.pages},
                    visiblePages: 5,
                    startPage:${qo.currentPage},
                    first: "首页",
                    prev: "上一页",
                    next: "下一页",
                    last: "尾页",
                    onPageClick: function (event, page) {
                        $("#currentPage").val(page);
                        $("#searchForm").submit();
                    }
                });

                $("#query").click(function () {
                    $("#currentPage").val(1);
                    $("#searchForm").submit();
                });
            });
            //监听新增按钮
            $("#addSystemDictionaryBtn").click(function () {
                $("#systemDictionaryModal").modal("show");
            });
            //保存按钮
            $("#saveBtn").click(function () {
                $("#editForm").ajaxSubmit({
                    datetype: "json",
                    success: function (data) {
                        if (data.success) {
                            $("#currentPage").val(1);
                            $("#searchForm").submit();
                        } else {
                            $.messager.popup(data.msg);
                        }
                    }
                });
            });
            //编辑字典
            $(".edit_Btn").click(function () {
                $("#editForm").clearForm(true);
                var data = $(this).data("json");
                $("#title").val(data.title);
                $("#sn").val(data.sn);
                $("#systemDictionaryId").val(data.id);
                $("#systemDictionaryModal").modal("show");
            });
        });
    </script>
</head>

<body>
<div class="container">
<#include "../common/top.ftl"/>
    <div class="row">
        <div class="col-sm-3">
        <#assign currentMenu = "systemDictionary" />
				<#include "../common/menu.ftl" />
        </div>
        <div class="col-sm-9">
            <div class="page-header">
                <h3>数据字典管理</h3>
            </div>
            <div class="row">
                <!-- 提交分页的表单 -->
                <form id="searchForm" class="form-inline" method="post" action="/systemDictionary_list.do">
                    <input type="hidden" name="currentPage" id="currentPage" value=""/>
                    <div class="form-group">
                    </div>
                    <div class="form-group">
                        <label>关键字</label>
                        <input class="form-control" type="text" name="keyword" value="${(qo.keyword)!''}">
                    </div>
                    <div class="form-group">
                        <button id="query" type="button" class="btn btn-success"><i class="icon-search"></i> 查询</button>
                        <a href="javascript:void(-1);" class="btn btn-success" id="addSystemDictionaryBtn">添加数据字典</a>
                    </div>
                </form>
            </div>
            <div class="row">
                <table class="table">
                    <thead>
                    <tr>
                        <th>名称</th>
                        <th>编码</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list pageResult.list as vo>
                    <tr>
                        <td>${vo.title}</td>
                        <td>${vo.sn}</td>
                        <td>
                            <a href="javascript:void(-1);" class="edit_Btn" data-json='${vo.jsonString}'>修改</a>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>

                <div style="text-align: center;">
                    <ul id="pagination" class="pagination"></ul>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="systemDictionaryModal" class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">编辑/增加</h4>
            </div>
            <div class="modal-body">
                <form id="editForm" class="form-horizontal" method="post" action="/systemDictionary_update.do"
                      style="margin: -3px 118px">
                    <input id="systemDictionaryId" type="hidden" name="id" value=""/>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">名称</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="title" name="title" placeholder="字典分类名称">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">编码</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="sn" name="sn" placeholder="字典分类编码">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <a href="javascript:void(0);" class="btn btn-success" id="saveBtn" aria-hidden="true">保存</a>
                <a href="javascript:void(0);" class="btn" data-dismiss="modal" aria-hidden="true">关闭</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>