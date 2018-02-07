<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>蓝源Eloan-P2P平台</title>
    <!-- 包含一个模板文件,模板文件的路径是从当前路径开始找 -->
<#include "common/links-tpl.ftl" />
    <script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
    <link type="text/css" rel="stylesheet" href="/css/account.css"/>

    <script type="text/javascript">

    </script>
</head>
<body>
<!-- 网页顶部导航 -->
<#include "common/head-tpl.ftl" />
<!-- 网页导航 -->
<!-- 在当前的freemarker的上下文中,添加一个变量,变量的名字叫nav,变量的值叫personal -->
<#assign currentNav="personal"/>
<#include "common/navbar-tpl.ftl" />

<div class="container">
    <div class="row">
        <!--导航菜单-->
        <div class="col-sm-3">
        <#assign currentMenu="personal"/>
					<#include "common/leftmenu-tpl.ftl" />
        </div>
        <!-- 功能页面 -->
        <div class="col-sm-9">
            <div class="panel panel-default">
                <div class="panel-body el-account">
                    <div class="el-account-info">
                        <div class="pull-left el-head-img">
                            <img class="icon" src="/images/ms.png"/>
                        </div>
                        <div class="pull-left el-head">
                            <p>用户名：${logininfo.username}</p>
                            <p>最后登录时间：2017-08-25 15:30:10</p>
                        </div>
                        <div class="pull-left" style="text-align: center;width: 400px;margin:30px auto 0px auto;">
                            <a class="btn btn-primary btn-lg" href="/recharge.do">账户充值</a>
                            <a class="btn btn-danger btn-lg" href="/moneyWithdraw.do">账户提现</a>
                        </div>
                        <div class="clearfix"></div>
                    </div>

                    <div class="row h4 account-info">
                        <div class="col-sm-4">
                            账户总额：<span class="text-primary">${account.totalAmount?string('0.00')}元</span>
                        </div>
                        <div class="col-sm-4">
                            可用金额：<span class="text-primary">${account.usableAmount?string('0.00')}元</span>
                        </div>
                        <div class="col-sm-4">
                            冻结金额：<span class="text-primary">${account.freezedAmount?string('0.00')}元</span>
                        </div>
                    </div>

                    <div class="row h4 account-info">
                        <div class="col-sm-4">
                            待收利息：<span class="text-primary">${account.unReceiveInterest?string('0.00')}元</span>
                        </div>
                        <div class="col-sm-4">
                            待收本金：<span class="text-primary">${account.unReceivePrincipal?string('0.00')}元</span>
                        </div>
                        <div class="col-sm-4">
                            待还金额：<span class="text-primary">${account.unReturnAmount?string('0.00')}元</span>
                        </div>
                    </div>

                    <div class="el-account-info top-margin">
                        <div class="row">
                            <div class="col-sm-4">
                                <div class="el-accoun-auth">
                                    <div class="el-accoun-auth-left">
                                        <img src="images/shiming.png"/>
                                    </div>
                                    <div class="el-accoun-auth-right">
                                        <h5>实名认证</h5>
                                        <p>
                                            未认证
                                            <a href="/realAuth.do" id="">立刻绑定</a>
                                        </p>
                                    </div>
                                    <div class="clearfix"></div>
                                    <p class="info">实名认证之后才能在平台投资</p>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <div class="el-accoun-auth">
                                    <div class="el-accoun-auth-left">
                                        <img src="images/shouji.jpg"/>
                                    </div>
                                    <div class="el-accoun-auth-right">
                                        <h5>手机认证</h5>
                                    <#if userinfo.hasBindPhone>
                                        <p>
                                            已认证
                                            <a href="javascript">查看</a>
                                        </p>
                                    <#else >
                                        <p>
                                            未认证
                                            <a href="javascript:;" id="showBindPhoneModal">立刻绑定</a>
                                        </p>

                                    </#if>
                                    </div>
                                    <div class="modal fade" id="bindPhoneModal" tabindex="-1" role="dialog"
                                         aria-labelledby="exampleModalLabel">
                                        <div class="modal-dialog" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal"
                                                            aria-label="Close"><span aria-hidden="true">&times;</span>
                                                    </button>
                                                    <h4 class="modal-title" id="exampleModalLabel">绑定手机</h4>
                                                </div>
                                                <div class="modal-body">
                                                    <form class="form-horizontal" id="bindPhoneForm" method="post"
                                                          action="/bindPhone.do">
                                                        <div class="form-group">
                                                            <label for="phoneNumber"
                                                                   class="col-sm-2 control-label">手机号:</label>
                                                            <div class="col-sm-4">
                                                                <input type="text" class="form-control" id="phoneNumber"
                                                                       name="phoneNumber"/>
                                                            </div>
                                                            <div class="col-sm-4">
                                                                <button id="sendVerifyCode" class="btn btn-primary"
                                                                        type="button" autocomplate="off">发送验证码
                                                                </button>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="verifyCode"
                                                                   class="col-sm-2 control-label">验证码:</label>
                                                            <div class="col-sm-4">
                                                                <input type="text" class="form-control" id="verifyCode"
                                                                       name="verifyCode"/>
                                                            </div>
                                                        </div>
                                                    </form>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-default" data-dismiss="modal">
                                                        取消
                                                    </button>
                                                    <button type="button" class="btn btn-primary" id="bindPhone">保存
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <script type="text/javascript">
                                        $(function () {
                                        <#--手机绑定-->
                                            $("#showBindPhoneModal").click(function () {
                                                $("#bindPhoneModal").modal("show");
                                            });
                                            //发送验证码按钮
                                            $("#sendVerifyCode").click(function () {
                                                var thisObj = $(this);
                                                thisObj.prop("disabled", true);
                                                //发送请求
                                                $.ajax({
                                                    url: '/sendVerifyCode',
                                                    data: {phoneNumber: $("#phoneNumber").val()},
                                                    dataType: "json",
                                                    success: function (data) {
                                                        $.messager.popup(data.msg);
                                                        if (data.success) {
                                                            var count = 5;
                                                            var timer = window.setInterval(function () {
                                                                count--;
                                                                if (count > 0) {
                                                                    thisObj.text(count + "s后重新发送");
                                                                } else {
                                                                    window.clearInterval(timer);
                                                                    thisObj.prop("disabled", false);
                                                                    thisObj.text("发送验证码")
                                                                }
                                                            }, 1000);
                                                        } else {
                                                            thisObj.prop("disabled", false);
                                                        }
                                                    }
                                                });
                                                //提交手机认证表单
                                                $("#bindPhone").click(function () {
                                                    $("#bindPhoneForm").ajaxSubmit({
                                                        dataType: "json",
                                                        success: function (data) {
                                                            if (data.success) {
                                                                window.location.reload();
                                                            } else {
                                                                $.messager.popup(data.msg);
                                                            }
                                                        }
                                                    });
                                                })
                                            });7
                                            /*邮箱认证*/
                                            $('#showBindEmailModal').click(function () {
                                                $('#bindEmailModal').modal("show");
                                            });
                                            //提交邮箱绑定表单
                                            $('#bindEmail').click(function () {
                                                $('#bindEmailForm').ajaxSubmit({
                                                    dataType:'json',
                                                    success:function (data) {
                                                        $.messager.popup(data.msg);
                                                        if(data.success){
                                                            $('#bindEmailModal').modal("hide");
                                                        } else {
                                                            $.messager.popup(data.msg);
                                                        }
                                                    }
                                                });
                                            });
                                        });


                                    </script>
                                    <div class="clearfix"></div>
                                    <p class="info">可以收到系统操作信息,并增加使用安全性</p>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <div class="el-accoun-auth">
                                    <div class="el-accoun-auth-left">
                                        <img src="images/youxiang.jpg"/>
                                    </div>
                                    <div class="el-accoun-auth-right">
                                        <h5>邮箱认证</h5>
                                    <#if userinfo.hasBindEmail>
                                        <p>
                                            已绑定
                                            <a href="#">查看</a>
                                        </p>
                                    <#else>
                                        <p>
                                            未绑定
                                            <a href="javascript:;" id="showBindEmailModal">去绑定</a>
                                        </p>
                                    </#if>
                                    </div>
                                    <div class="modal fade" id="bindEmailModal" tabindex="-1" role="dialog"
                                         aria-labelledby="exampleModalLabel">
                                        <div class="modal-dialog" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal"
                                                            aria-label="Close"><span aria-hidden="true">&times;</span>
                                                    </button>
                                                    <h4 class="modal-title" id="exampleModalLabel">绑定邮箱</h4>
                                                </div>
                                                <div class="modal-body">
                                                    <form class="form-horizontal" id="bindEmailForm" method="post"
                                                          action="/sendEmail.do">
                                                        <div class="form-group">
                                                            <label for="email"
                                                                   class="col-sm-2 control-label">Email:</label>
                                                            <div class="col-sm-4">
                                                                <input type="text" class="form-control" id="email"
                                                                       name="email"/>
                                                            </div>
                                                        </div>
                                                    </form>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-default" data-dismiss="modal">
                                                        取消
                                                    </button>
                                                    <button type="button" class="btn btn-primary" id="bindEmail">保存
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="clearfix"></div>
                                    <p class="info">您可以设置邮箱来接收重要信息</p>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-4">
                                <div class="el-accoun-auth">
                                    <div class="el-accoun-auth-left">
                                        <img src="images/baozhan.jpg"/>
                                    </div>
                                    <div class="el-accoun-auth-right">
                                        <h5>VIP会员</h5>
                                        <p>
                                            普通用户
                                            <a href="#">查看</a>
                                        </p>
                                    </div>
                                    <div class="clearfix"></div>
                                    <p class="info">VIP会员，让你更快捷的投资</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<#include "common/footer-tpl.ftl" />
</body>
</html>