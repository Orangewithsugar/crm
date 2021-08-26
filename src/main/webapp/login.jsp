<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme()+"://" +
					request.getServerName() + ":" + request.getServerPort() +
					request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

	<script>

		$(function () {

			// 如果当前窗口不是top顶层窗口，我需要将顶层窗口设置为当前窗口。  不用背这个，理解即可
			if(window.top!=window){
				window.top.location=window.location;
			}

			// 页面加载完毕后，刷新后，将用户文本框中的内容清空
			$("#loginAct").val("");


			// 页面加载完毕后，让用户名的文本框自动获得焦点
			$("#loginAct").focus();


			$("#submitBtn").click(function () {

				login();
			})


			// 为当前登录窗口绑定敲键盘事件
			// event：这个参数可以取得我们敲的是哪个键
			$(window).keydown(function (event) {

				// alert(event.keyCode);

				// 如果取得的键位的码值为13，说明敲的是回车键。执行登录操作
				if(event.keyCode==13){
					login();
				}

			})

		})
		// 普通的自定义的function方法，一定要写在 $(function(){}) 的外面
		function login(){

			// alert("验证登录操作~~")

			// 1、验证账号密码不能为空
			// 取得账号密码
			// 将文本中的左右空格去掉，使用 $.trim(文本)
			var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());

			if(loginAct=="" || loginPwd==""){

				$("#msg").html("账号密码不能是空的yo");
				// 如果账号密码为空，则需要及时强制终止该方法
				return false;
			}

			// 2、拿着填写好的账号密码，到数据库中验证账号密码是否正确
			// 去后台验证登录相关操作，向后台发送一个ajax请求（异步刷新）
			$.ajax({

				url : "settings/user/login.do",
				data : {

					"loginAct" : loginAct,
					"loginPwd" : loginPwd

				},
				type : "post",
				dataType : "json",
				success : function (data) {

					/*
						data
							前端想要什么
								{"success":true/false,"msg":哪里错了}  知道验证成功还是失败，如果失败的话得返回是具体那种验证失败

					 */
					// 如果登录成功
					if(data.success){

						// 跳转到工作台的初始页(欢迎页)
						window.location.href = "workbench/index.jsp";

					}else{ // 如果登录失败，失败的提示信息

						$("#msg").html(data.msg);
					}

				}

			})

		}

	</script>

</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image\guimie3.png" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRMzy &nbsp;<span style="font-size: 12px;">&copy;2021&nbsp;宗一</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">

							<!--这个span标签就是登录错误提示信息 的位置-->
							<span id="msg" style="color:red"></span>
						
					</div>

					<!--
						注意：按钮写在form表单中，默认的行为就是提交表单，一定要将按钮的类型设置为button
							 按钮所触发的行为，应该由我们自己手动写js代码来实现
					-->

					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>