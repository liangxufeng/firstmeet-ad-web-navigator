// 用户管理的js文件
$(document).ready(function () {
  initTable('');

  // 添加用户按钮
  $('#add-btn').on('click', function () {
    $("#admin-title").text("添加用户");
    $("#id").val('');//把隐藏域id置空
    $("#userDeptId").val('');//把隐藏部门id置空
    $('#userForm').bootstrapValidator('destroy', true);//把校验数据清空
    $("#userForm")[0].reset();//每次增加先把表单数据置空
    var cityObj = $("#citySel");
    cityObj.attr("value", "");//把部门数据清空
    setEnable("#userId", true);
    toggleDialog(true);
  });

  // 选择部门按钮
  $('#changeUserDept').on('click', function () {
    initTree();
  });

  //重置按钮
  $('#reset').on('click', function () {
    $('#searchUserName').val('');
    $("#userTable").bootstrapTable('destroy');
    initTable('')
  });

  // 关闭遮罩层按钮
  $('.websetContClose').on('click', function () {
    toggleDialog(false);
  });
// 模糊查询
  $('#search').on('click', function () {
    var userName = $("#searchUserName").val();
    $("#userTable").bootstrapTable('destroy');
    initTable(userName);
  })
});
var initTable = function (userName) {
  var getUserUrl = $('#getUserUrl').attr('href');
  $('#userTable').bootstrapTable({
    contentType: "application/x-www-form-urlencoded;charset=UTF-8",
    url: getUserUrl,                      //请求后台的URL（*）
    method: 'GET',                      //请求方式（*）
    //toolbar: '#toolbar',              //工具按钮用哪个容器
    striped: true,                      //是否显示行间隔色
    cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
    pagination: true,                   //是否显示分页（*）
    sortable: false,                     //是否启用排序
    sortOrder: "asc",                   //排序方式
    sidePagination: "server",           //分页方式：client用户分页，server服务端分页（*）
    pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
    pageSize: 10,                     //每页的记录行数（*）
    pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
    search: false,                      //是否显示表格搜索
    strictSearch: false,
    height: 600,
    showColumns: true,                  //是否显示所有的列（选择显示的列）
    showRefresh: true,                  //是否显示刷新按钮
    minimumCountColumns: 2,             //最少允许的列数
    clickToSelect: true,                //是否启用点击选中行
    uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
    showToggle: true,                   //是否显示详细视图和列表视图的切换按钮
    cardView: false,                    //是否显示详细视图
    detailView: false,                  //是否显示父子表
    smartDisplay: false,                 //智能显示
    //得到查询的参数
    queryParams: function (params) {
      var temp = {
        user_name: userName,
        page_size: params.limit,
        page: (params.offset / params.limit) + 1,   //页码
        // sort: params.sort,      //排序列名
        // sortOrder: params.order //排位命令（desc，asc）
      };
      return temp;
    },
    columns: [{
      checkbox: true,
      visible: true                  //是否显示复选框
    }, {
      field: 'userId',
      title: '用户账号'
    }, {
      field: 'userName',
      title: '用户名称'
    }, {
      field: 'userDeptName',
      title: '所属部门',
      sortable: true
    }, {
      field: 'createdAt',
      title: '创建时间'
    }, {
      field: 'updatedAt',
      title: '修改时间'
    }],
    onLoadSuccess: function () {
    },
    responseHandler: function (res) {
      var data = {
        total: res.total_elements,
        rows: res.user
      };

      return data;
    },
    onLoadError: function () {
      // showTips("数据加载失败！");
    },
    onDblClickRow: function (row, $element) {

      var id = row.userId;
      editUser(id);
    }
  });
};
$('#btn-sure-close').on('click', function () {
  toggleDialog(false);
});
$("#btn-sure-add").click(function () {
  $('#userForm').bootstrapValidator({
    message: 'This value is not valid',
    feedbackIcons: {
      valid: 'glyphicon glyphicon-ok',
      invalid: 'glyphicon glyphicon-remove',
      validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
      userId: {
        validators: {
          notEmpty: {
            message: '用户账号不能为空'
          }
        }
      },
      userName: {
        validators: {
          notEmpty: {
            message: '用户名称不能为空'
          }
        }
      },
      userDept: {
        validators: {
          notEmpty: {
            message: '所属部门不能为空'
          }
        }
      },
      userSecret: {
        validators: {
          notEmpty: {
            message: '用户密码不能为空'
          },
          stringLength: {
            min: 6,
            max: 30,
            message: '用户密码长度不能小于6位或超过30位'
          }
        }
      },
      confirmUserSecret: {
        validators: {
          notEmpty: {
            message: '确认用户密码不能为空'
          },
          identical: {  //比较是否相同
            field: 'userSecret',  //需要进行比较的input name值
            message: '两次密码不一致'
          }
        }
      }
    }
  });

  if (!$('#userForm').data('bootstrapValidator').isValid()) {
    return;
  }
  var userDept = $("#userDeptId").val();
  if (userDept === "") {
    if ($("#citySel").val() === "") {
      alert("用户部门不能为空");
      return;
    } else {
      userDept = $("#citySel").val();
    }
  }
  var userName = $("#userName").val();
  var userSecret = $("#userSecret").val();
  var userId = $("#userId").val();
  var saveUserUrl = $('#saveUserUrl').attr('href');
  var id = $("#id").val();
  $.ajax({
    type: 'POST',
    url: saveUserUrl,
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    dataType: 'json',
    data: {
      'user_id': userId,
      'user_name': userName,
      'user_secret': userSecret,
      'user_dept': userDept,
      'id': id
    },
    success: function (data) {
      if (data.status == "ok") {
        alert("保存成功");
        $('#myModal').modal('hide');
        toggleDialog(false);
        $("#userTable").bootstrapTable('refresh');
      } else if (data.status == "addFail") {
        alert("该用户已经存在！")
      } else if (data.status == "addUserIdFail") {
        alert("该用户账号已经存在！")
      } else {
        alert("保存失败，请联系技术")
      }
    },
    error: function (data) {
      alert("保存失败")
    }
  })
})
$("#del-btn").on('click', function () {
  var rows = $("#userTable").bootstrapTable('getSelections');
  var userIdArray = new Array()
  if (rows.length <= 0) {
    alert("请选择至少一行数据删除");
    return;
  }
  if (confirm("确认删除该用户及其拥有的部门、角色等信息吗？")) {
    for (var i = 0; rows && i < rows.length; i++) {
      var row = rows[i];
      if (row.userId === 'admin') {
        alert("admin不可删除!");
        return;
      }
      userIdArray.push(row.userId);
    }

    var deleteUserUrl = $('#deleteUserUrl').attr('href');
    $.ajax({
      type: 'POST',
      url: deleteUserUrl,
      contentType: 'application/x-www-form-urlencoded; charset=utf-8',
      dataType: 'json',
      data: {
        'user_ids': userIdArray
      },
      success: function (data) {
        if (data.status == "ok") {
          alert("删除成功");
          $("#userTable").bootstrapTable('refresh');
        } else if (data.status == "no_delete") {
          alert("admin不可删除");
        } else {
          alert("删除失败");
        }
      },
      error: function (data) {
        alert("删除失败");
      }
    })
  }
})
$("#edit-btn").on('click', function () {
  var rows = $("#userTable").bootstrapTable('getSelections');
  if (rows.length <= 0 || rows.length > 1) {
    alert("请选择一行数据进行编辑");
    return;
  }
  $('#myModal').modal();
  editUser(rows[0].userId);

})

function editUser(userId) {
  var user = $('#tokenUserId').val();
  if (userId === 'admin' && user !== 'admin') {
    return;
  }
  var editUserUrl = $('#editUserUrl').attr('href');
  $("#admin-title").text("编辑用户");
  $.ajax({
    type: 'GET',
    url: editUserUrl,
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    dataType: 'json',
    data: {
      'user_id': userId
    },
    success: function (data) {
      if (data != null) {
        $("#id").val(data.id);
        $("#userName").val(data.userName);
        $("#userSecret").val(data.userPassword);
        $("#confirmUserSecret").val(data.userPassword);
        $("#userId").val(data.userId);
        $("#userDeptId").val(data.userDeptId);
        var userDeptName = data.userDept;
        var cityObj = $("#citySel");
        cityObj.attr("value", userDeptName);
      }
      $('#userForm').bootstrapValidator('destroy', true);//把校验数据清空
      setEnable("#userId", false);
      toggleDialog(true);
    },
    error: function (data) {
    }
  })
}

//给用户添加角色
$("#userRole-btn").on('click', function () {
  var rows = $("#userTable").bootstrapTable('getSelections');
  if (rows.length <= 0 || rows.length > 1) {
    alert("请选择一条用户数据");
    return;
  }
  if (rows[0].userId === 'admin') {
    alert("admin不可分配角色!");
    return;
  }
  //接下来要执行将选中的id作为参数传递到后台
  location.href = "user_role?userId=" + rows[0].userId;
});
/*=====================zTree开始=========================*/
var setting = {
  view: {
    dblClickExpand: false
  },
  data: {
    key: {
      name: "deptName"
    },
    simpleData: {
      enable: true,
      idKey: "deptId",
      pIdKey: "deptParentId",
      rootPId: "0"
    }
  },
  callback: {
    onClick: onClick,
    onDblClick: zTreeOnDblClick
  }
};

function zTreeOnDblClick(e, treeId, treeNode) {
  var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
      nodes = zTree.getSelectedNodes(),
      v = "";
  nodes.sort(function compare(a, b) {
    return a.id - b.id;
  });
  for (var i = 0, l = nodes.length; i < l; i++) {
    var pn = nodes[i].getParentNode();
    if (pn === null) {
      v += "";
    } else {
      v += pn.deptName + "-";
    }
    v += nodes[i].deptName + "-";
    var d = nodes[i].deptId;
    var userDeptId = $("#userDeptId");
    userDeptId.attr("value", d);
  }
  if (v.length > 0) {
    v = v.substring(0, v.length - 1);
  }
  var cityObj = $("#citySel");
  cityObj.attr("value", v);
  hideMenu();
}

function onClick(e, treeId, treeNode) {
  var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
      nodes = zTree.getSelectedNodes(),
      v = "";
  nodes.sort(function compare(a, b) {
    return a.id - b.id;
  });
  for (var i = 0, l = nodes.length; i < l; i++) {
    var pn = nodes[i].getParentNode();
    if (pn === null) {
      v += "";
    } else {
      v += pn.deptName + "-";
    }
    v += nodes[i].deptName + "-";
    var d = nodes[i].deptId;
    var userDeptId = $("#userDeptId");
    userDeptId.attr("value", d);
  }
  if (v.length > 0) {
    v = v.substring(0, v.length - 1);
  }
  var cityObj = $("#citySel");
  cityObj.attr("value", v);
}

function showMenu() {
  var cityObj = $("#citySel");
  var cityOffset = $("#citySel").offset();
  $("#menuContent").css({left: "40px", top: "277px"}).slideDown("fast");
  $("body").bind("mousedown", onBodyDown);
}

function hideMenu() {
  $("#menuContent").fadeOut("fast");
  $("body").unbind("mousedown", onBodyDown);
}

function onBodyDown(event) {
  if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(
      event.target).parents("#menuContent").length > 0)) {
    hideMenu();
  }
}

$("#menuBtn").on('click', function () {
  var getUserDeptUrl = $('#getUserDeptUrl').attr('href');
  $.ajax({
    url: getUserDeptUrl,
    method: "GET",
    dataType: "json",
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    success: function (data) {
      var tree = $.fn.zTree.init($("#treeDemo"), setting, data);
      var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
      var nodes = treeObj.getNodes();
      if (nodes.length > 0) {
        for (var i = 0; i < nodes.length; i++) {
          treeObj.expandNode(nodes[i], true, false, false);//默认展开第一级节点
        }
      }
    },
    error: function () {
      alert("加载失败");
    }
  });
});
/*====================zTree结束==========================*/