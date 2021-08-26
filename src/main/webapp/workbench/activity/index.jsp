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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


<script type="text/javascript">

	$(function(){
		
		// 为创建按钮绑定事件，打开添加操作的模态窗口
		$("#addBtn").click(function () {


			//
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});



			/*

				操作模态窗口的方式：

					需要操作的模态窗口的jquery对象，调用 modal 对象，为该方法传递参数：
						（1）show：打开模态窗口
						（2）hide：关闭模态窗口

			 */

			// alert("222");
			// $("#createActivityModal").modal("show");

			// 需求：去后台走一趟，把用户信息取出来，然后显示在下拉列表中
			$.ajax({

				url : "workbench/activity/getUserList.do" ,

				type : "get" ,
				dataType : "json" ,
				success : function ( data ) {

					// 获取到的是一个json串格式的一堆用户
					// 遍历每一个json串中的json串，把对应用户的名字赋给下拉列表的html标签中
					var html = "<option></option>"
					// 每一个n就是一个user对象
					$.each(data , function(i , n){

						html += "<option value = '" + n.id + "'>" + n.name + "</option>"

					})

					// 取得拼接好的下拉列表的字符串之后，给对应的那个select标签赋过去
					$("#create-owner").html(html);



					// 将当前登录的用户，设置为下拉框默认的选项
					/*

						<select id="create-owner">
							<option value="06f5fc056eac41558a964f96daa7f27c">李四</option>
							<option value="40f6cdea0bd34aceb77492a1656d9fb3">张三</option>
							<option value="eccbc87e4b5ce2fe28308fd9f2a7baf3">宗一</option>
							<option value="a87ff679a2f3e71d9181a67b7542122c">宗一来喽</option>
						</select>

						$("#create-owner").val("eccbc87e4b5ce2fe28308fd9f2a7baf3");
					 */
					// 从session域中取到已登录用户的名字，使用EL 表达式
					var id = "${user.id}";
					$("#create-owner").val(id);


					// 然后展示模态窗口即可
					$("#createActivityModal").modal("show");

				}

			})
		})


		// 点击【创建模态窗口】的【保存】按钮之后：发送表单中用户已填写的数据给后端，后端执行添加操作，返回一个是否成功的标志。前端再根据这个标志执行不同的操作
		$("#saveBtn").click(function () {

			// 发送一个ajax请求
			$.ajax({

				url : "workbench/activity/save.do",
				data : {

					// 把用户填写到表单中的数据发送过去。 注意把前后空格删除一下
					"owner" :  $.trim($("#create-owner").val()) ,
					"name" :  $.trim($("#create-name").val()) ,
					"startDate" :  $.trim($("#create-startDate").val()) ,
					"endDate" :  $.trim($("#create-endDate").val()) ,
					"cost" :  $.trim($("#create-cost").val()) ,
					"description" :  $.trim($("#create-description").val())


				},
				type : "post",
				dataType : "json",
				success : function (data) {

					/*
						data:
							{"success":true/false}   来说明添加成功或者失败
					 */
					if(data.success){

						// 执行添加-成功

						// 刷新市场活动信息列表（局部刷新）
						// pageList(1,2);

						/*
							  $("#activityPage").bs_pagination('getOption', 'currentPage')
									表示操作后停留在当前页
							  $("#activityPage").bs_pagination('getOption', 'rowsPerPage'))
									表示操作后维持已经设置好的每页展现的记录数

							   这两个参数不需要我们进行任何的修改操作，直接使用即可。

						 */

						// 做完添加操作后，应该回到第一页，维持每页展现的记录数
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


						// 清空【添加操作】模态窗口中的数据
						// 提交表单： $("#activityAddForm").submit();

						/*
							注意：
								我们拿到了form表单的jquery对象，对于表单的jquery对象，提供了submit()方法，让我们可以提交表单。
								但是这个表单的jquery对象，没有为我们提供reset()方法让我们重置表单（坑：IDEA却提示有reset()方法）

								虽然jquery对象没有为我们提供这个reset()方法，但是原生的js为我们提供了reset方法。
								所以我们要将jquery对象转换为原生js对象（dom对象）。

							jquery转换为js的dom对象：
								使用jquery对象的时候，可以将jquery对象当做dom对象的数组来使用。
								从数组中取得某一个元素。
									（1）jquery对象[下标]            我们这个jquery对象中只有一个dom对象，就是这个表单。所以下标是0



							dom对象转换为jquery对象：
									（1）$(dom)
						 */
						// 【清空模态窗口的表单数据】
						$("#activityAddForm")[0].reset();


						// 自动关闭模态窗口
						$("#createActivityModal").modal("hide");



					} else {

						// 执行添加-失败
						alert("添加【市场活动】——失败~~")

					}
				}
			})
		})



		// 页面加载完毕后，触发一个方法：去后台取数据，然后局部刷新出来我们的市场活动列表
		// 默认展开列表的第一页，每页展现两条记录
		pageList(1,2);


		// ______________________________________________________________________点击上面的条件【查询】按钮，触发pageList()方法
		$("#searchBtn").click(function () {

			/*

				点击查询按钮的时候，我们应该将搜索框中的信息保存起来。保存到隐藏域中

			 */
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));


			pageList(1,2);

		})



		// _____________________________________________________________________________给全选的复选框绑定事件，触发全选操作
		$("#qx").click(function () {

			$("input[name=xz]").prop("checked" , this.checked);

		})

		/*$("input[name=xz]").click()(function () {

			alert("123");

		})*/

		// _________________________________________________________________________下面单选框全部打勾的时候，全选框要自动打勾
		$("#activityBody").on("click" , $("input[name=xz]") , function () {

			// 原理：比如当前页面有2条记录，如果满足 打勾的数量=2(当前页面的记录数)，那么就说明当前页面的单选框全打勾了，此时全选框应该自动打勾
			$("#qx").prop("checked" , $("input[name=xz]").length == $("input[name=xz]:checked").length);

		})


		// _________________________________________________________________________为删除按钮绑定事件，执行市场活动的删除操作
		$("#deleteBtn").click(function () {

			// 1. 找到复选框中所有挑√的复选框的jquery对象
			var $xz = $("input[name=xz]:checked");



			if ($xz.length == 0){  // jquery是元素为dom对象的数组

				alert("你啥都没选  我咋删除？");

			} else {
				// 肯定选了，有可能是1条，有可能是多条
				// url:workbench/activity/delete.do?id=xxx&id=xxx&id=xxx


				if(confirm("确定删除所选中的记录吗？")){

					// 拼接上面的参数
					var param = "";

					for (var i=0 ; i<$xz.length ; i++){

						// $($xz[i]).val()    $xz[i]， $xz是jquery对象，$xz[i]是dom对象。
						// 我们先拿到其中某一个dom对象，然后再把这个dom对象转换成jquery对象，调用其val方法
						// 也可以写成  $xz[i].value;  但是用另一种方法方便我们记忆。就用另一种方法吧。
						param += "id="+$($xz[i]).val();

						// 如果不是最后一个元素，需要在后面追加一个&符号
						if(i<$xz.length-1){

							param += "&";

						}


					}

					// alert(param);
					$.ajax({

						url : "workbench/activity/delete.do",
						data : param ,
						type : "post",
						dataType : "json",
						success : function (data) {

							// data  {"success":true/false}
							if (data.success){

								// 删除成功
								// 回到第一页，维持每页展现的记录数
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

							} else {

								alert("删除市场活动失败");

							}
						}
					})
				}
			}
		})

		// ___________________________________________________________________________________为修改按钮绑定事件，打开修改模态窗口
		$("#editBtn").click(function () {

			var $xz = $("input[name=xz]:checked");

			if($xz.length == 0){

				alert("你要修改空气吗z~");

			} else if($xz.length > 1){

				alert("一回只能修改一条喔z~");

			} else {

				// 肯定只选了一条

				// 因为jquery对象里面只有一个dom对象（相当于数组里面只有一个元素），所以可以直接val()，取值。 拿到要修改的那条记录的id
				var id = $xz.val();

				$.ajax({

					url : "workbench/activity/getUserListAndActivity.do",
					data : {

						"id" : id

					},
					type : "get",
					dataType : "json",
					success : function (data) {

						/*
							data
								用户列表
								市场活动对象

							{"uList":[{用户1},{2},{3}],"a":{市场活动}}
						 */

						// 1. 处理所有者下拉框（注意里面的默认用户是上次这个地方选择过的用户，不需要我们改动。我们打开之后就能知道上次是哪个用户修改的本条记录）
						var html = "<option></option>";

						$.each(data.uList , function ( i , n) {

							html += "<option value='"+ n.id +"'>"+ n.name +"</option>";

						})

						$("#edit-owner").html(html);


						// 2. 处理单条activity
						$("#edit-id").val(data.a.id);
						$("#edit-name").val(data.a.name);
						$("#edit-owner").val(data.a.owner);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);


						// 3. 所有的值都填写好之后，打开修改操作的模态窗口
						$("#editActivityModal").modal("show");


					}

				})

			}


		})


		// _________________________________________________________________________________________为【更新】按钮绑定事件，执行市场活动的修改操作
		$("#updateBtn").click(function () {

			// 发送一个ajax请求
			$.ajax({

				url : "workbench/activity/update.do",
				data : {

					// 把用户填写到表单中的数据发送过去。 注意把前后空格删除一下
					"id" :  $.trim($("#edit-id").val()) ,
					"owner" :  $.trim($("#edit-owner").val()) ,
					"name" :  $.trim($("#edit-name").val()) ,
					"startDate" :  $.trim($("#edit-startDate").val()) ,
					"endDate" :  $.trim($("#edit-endDate").val()) ,
					"cost" :  $.trim($("#edit-cost").val()) ,
					"description" :  $.trim($("#edit-description").val())


				},
				type : "post",
				dataType : "json",
				success : function (data) {

					/*
						data:
							{"success":true/false}   来说明添加成功或者失败
					 */
					if(data.success){

						// 执行修改-成功

						// 刷新市场活动信息列表（局部刷新）
						// pageList(1,2);

						// 修改操作后，应该维持在当前页，展现每页展现的记录数
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


						// 【清空模态窗口的表单数据】
						// $("#activityAddForm")[0].reset();

						// 自动关闭模态窗口
						$("#editActivityModal").modal("hide");


					} else {

						// 执行添加-失败
						alert("修改【市场活动】——失败~~")

					}
				}
			})


		})







	});


	// =====================================================================================================上面是自动执行，下面是手写函数

	/*
		对于所有的关系型数据库，做前端的分页相关操作的基础组件。
		就是【pageNo】和【pageSize】
			（1）pageNo: 页码
			（2）pageSize: 每页展现的记录数

		pageList方法：就是发出ajax请求到后台（这个过程是自动产生的），从后台取得最新的市场活动消息列表数据。
					 通过响应回来的数据，局部刷新市场活动信息列表。

		我们在哪些情况下，需要调用pageList方法呢？（什么情况下需要刷新一下市场活动列表）
			（1）点击左侧菜单中的【市场活动】超链接，初始化刷新。调用pageList方法
			（2）【添加】【修改】【删除】后，需要刷新市场活动列表。调用pageList方法
			（3）点击【查询】按钮的时候，需要刷新市场活动列表。调用pageList方法
			（4）点击【分页组件】的时候，需要刷新市场活动列表。调用pageList方法

			以上为pageList()方法制定了6个入口，也就是说在以上6个操作执行完成后，我们必须调用pageList方法，刷新市场活动信息列表

	 */
	// __________________________________________________________________________________________________函数-分页查询
	function pageList(pageNo , pageSize) {

		// 将全选框框的勾勾取消掉
		$("#qx").prop("checked" , false);

		// 查询前，将隐藏域中保存的信息取出来，重新赋予到搜索框中。与之前的过程是相反的
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));


		$.ajax({

			url : "workbench/activity/pageList.do",
			data : {

				"pageNo" : pageNo ,
				"pageSize" : pageSize ,
				"name" : $.trim($("#search-name").val()) ,
				"owner" : $.trim($("#search-owner").val()) ,
				"startDate" : $.trim($("#search-startDate").val()) ,
				"endDate" : $.trim($("#search-endDate").val())

			},
			type : "get",
			dataType : "json",
			success : function (data) {

				/*
					data
						我们需要的：市场活动列表
							[{市场活动1},{2},{3}...]           List<Activity> aList
						一会分页插件需要的：查询出来的总记录数
						{"total":100}                        int total

						最终数据：

							{"total":100,"dataList",[{市场活动1},{2},{3}...]}

				 */
				var html = "";

				// 每一个n就是某一个市场活动
				$.each(data.dataList , function (i , n) {

					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+ n.id +'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+ n.name +'</a></td>';
					html += '<td>'+ n.owner +'</td>';
					html += '<td>'+ n.startDate +'</td>';
					html += '<td>'+ n.endDate +'</td>';
					html += '</tr>';

				})

				$("#activityBody").html(html);




				// 计算总页数
				var totalPages = data.total%pageSize==0 ? data.total/pageSize : parseInt(data.total/pageSize)+1;


				// 数据处理完毕后，结合分页查询，对前端展现分页信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数，是可以调整的
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数  之前获取过了

					visiblePageLinks: 3, // 显示几个卡片   就是下面的1、2、3那个小方块

					showGoToPage: true,     // 跳转到第几页
					showRowsPerPage: true,  // 显示的信息    设置为true，即让显示的信息越多越好
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					// 该回调函数是在：点击分页组件的时候触发的，触发我们上面写好的 pageList方法
					onChangePage : function(event, data){
						// 这行代码千万不要碰，是人家给我们提供好的  这个data也不是我们返回的那个data。但是这个方法调用的是我们上面写好的。
						pageList(data.currentPage , data.rowsPerPage);
					}
				});


			}

		})

	}
	
</script>
</head>
<body>

	<!--使用隐藏域，容纳上面4个搜索框中的用户输入的值。当用户点击【搜索】按钮之后，才把用户输入的值存到这里面-->
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form">


						<input type="hidden" id="edit-id"/>

						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">



								</select>
							</div>
							<label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-name" >
							</div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" >
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>

						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<!--

									关于文本域 textarea：

										（1）一定要以标签对的形式来呈现，正常状态下标签对要紧紧的挨着
										（2）textarea虽然是以标签对的形式来呈现的，但是他也是属于表单元素范畴
											我们所有的对于textarea的取值和赋值操作，应该统一使用val()方法，而不是html()方法
											（其实简单的textarea是可以用html方法的，但是复杂的textarea用html方法就不行了）
								-->
								<textarea class="form-control" rows="3" id="edit-description">222</textarea>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">

					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">

								<select class="form-control" id="create-owner">



								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" >
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" >
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<!--

						data-dismiss="modal" : 表示关闭模态窗口

					-->
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	

	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表22</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">

				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<!--

						data-toggle="modal"：
							表示触发该按钮，将要打开一个模态窗口

						data-target="#createActivityModal"：
							表示要打开哪个模态窗口，通过 #id 的方式找到该窗口

						现在我们是以属性值的方式写在了button元素中，用来打开模态窗口的。
						但是这样做是有问题的：
							问题在于没有办法对按钮的功能进行扩充

						所以未来的实际项目开发中，对于触发模态窗口的操作，一定不要写死在元素当中，应该由我们自己写js代码来操控。

					-->
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称22</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>--%>
                        </tr>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">

				<div id="activityPage"></div>

			</div>
			
		</div>
		
	</div>
</body>
</html>














