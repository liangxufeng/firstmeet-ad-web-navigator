// 角色管理的js文件
$(document).ready(function () {
  initTable('')

  // 添加角色按钮
  $('#add-btn').on('click', function () {
    $("#admin-title").text("添加角色");
    $("#roleId").val('');//把隐藏域id置空
    $('#roleForm').bootstrapValidator('destroy', true);//把校验数据清空
    $("#roleForm")[0].reset()//每次增加先把表单数据置空
    setEnable("#roleId", true);
    toggleDialog(true);
  })

  //重置按钮
  $('#reset').on('click', function () {
    $('#searchRoleName').val('');
    $("#roleTable").bootstrapTable('destroy');
    initTable('')
  })

  // 关闭遮罩层按钮
  $('.websetContClose').on('click', function () {
    toggleDialog(false);
  })

// 模糊查询
  $('#search').on('click', function () {
    var roleName = $("#searchRoleName").val();
    $("#roleTable").bootstrapTable('destroy');
    initTable(roleName);
  })
})

var initTable = function (roleName) {
  var getRoleUrl = $('#getRoleUrl').attr('href');
  $('#roleTable').bootstrapTable({
    contentType: "application/x-www-form-urlencoded;charset=UTF-8",
    url: getRoleUrl,                      //请求后台的URL（*）
    method: 'GET',                      //请求方式（*）
    //toolbar: '#toolbar',              //工具按钮用哪个容器
    striped: true,                      //是否显示行间隔色
    cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
    pagination: true,                   //是否显示分页（*）
    sortable: false,                     //是否启用排序
    sortOrder: "asc",                   //排序方式
    sidePagination: "server",           //分页方式：client角色分页，server服务端分页（*）
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
        role_name: roleName,
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
    },
      /*  {
          field: 'roleId',
          title: '角色ID'
        },*/
      {
        field: 'roleName',
        title: '角色名称'
      },
      {
        field: 'roleRemark',
        title: '角色说明'

      },
      {
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
        rows: res.role
      };

      return data;
    },
    onLoadError: function () {
      // showTips("数据加载失败！");
    },
    onDblClickRow: function (row, $element) {
      var id = row.roleId;
      editRole(id);
    },
  });
}

$('#btn-sure-close').on('click', function () {
  toggleDialog(false);
});

$("#btn-sure-add").click(function () {

  $('#roleForm').bootstrapValidator({
    message: 'This value is not valid',
    feedbackIcons: {
      valid: 'glyphicon glyphicon-ok',
      invalid: 'glyphicon glyphicon-remove',
      validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
      roleName: {
        validators: {
          notEmpty: {
            message: '角色名称不能为空'
          }
        }
      }
    }
  });
  if (!$('#roleForm').data('bootstrapValidator').isValid()) {
    return;
  }
  var roleName = $("#roleName").val();
  var roleRemark = $("#roleRemark").val();
  var roleId = $("#roleId").val();
  var saveRoleUrl = $('#saveRoleUrl').attr('href');
  $.ajax({
    type: 'POST',
    url: saveRoleUrl,
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    dataType: 'json',
    data: {
      'role_id': roleId,
      'role_name': roleName,
      'role_remark': roleRemark
    },
    success: function (data) {
      if (data.status == "ok") {
        alert("保存成功")
        toggleDialog(false);
        $("#roleTable").bootstrapTable('refresh');
      } else if (data.status == "addFail") {
        alert("该角色已经存在！")
      } else {
        alert("保存失败，请联系技术")
      }
    },
    error: function (data) {
      alert("保存失败")
    }
  })
})

//删除角色
$("#del-btn").on('click', function () {
  var rows = $("#roleTable").bootstrapTable('getSelections');
  var roleIdArray = new Array()
  if (rows.length <= 0) {
    alert("请选择至少一行数据删除");
    return;
  }
  if (confirm("确认删除该角色及其拥有的权限信息吗？")) {
    for (var i = 0; rows && i < rows.length; i++) {
      var row = rows[i];
      roleIdArray.push(row.roleId);
    }
    var deleteRoleUrl = $('#deleteRoleUrl').attr('href');
    $.ajax({
      type: 'POST',
      url: deleteRoleUrl,
      contentType: 'application/x-www-form-urlencoded; charset=utf-8',
      dataType: 'json',
      data: {
        'role_ids': roleIdArray
      },
      success: function (data) {
        if (data.status == "ok") {
          alert("删除成功");
          $("#roleTable").bootstrapTable('refresh');
        } else if (data.status == "no_delete") {
          alert("初见后台管理员不可删除");
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
  var rows = $("#roleTable").bootstrapTable('getSelections');
  if (rows.length <= 0 || rows.length > 1) {
    alert("请选择一行数据进行编辑");
    return;
  }
  editRole(rows[0].roleId);
})

//编辑client
function editRole(roleId) {
  var editRoleUrl = $('#editRoleUrl').attr('href');
  $("#admin-title").text("编辑角色");
  $.ajax({
    type: 'GET',
    url: editRoleUrl,
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    dataType: 'json',
    data: {
      'role_id': roleId
    },
    success: function (data) {
      if (data != null) {//数据回显
        $("#roleName").val(data.roleName);
        $("#roleRemark").val(data.roleRemark);
        $("#roleId").val(data.roleId);
      }
      $('#roleForm').bootstrapValidator('destroy', true);//把校验数据清空

      toggleDialog(true);
    },
    error: function (data) {
    }
  })
}

//给角色添加系统权限
$("#rolePer-btn").on('click', function () {
  var rows = $("#roleTable").bootstrapTable('getSelections');
  if (rows.length <= 0 || rows.length > 1) {
    alert("请选择一条角色信息");
    return;
  }
  //接下来要执行将选中的id作为参数传递到后台
  location.href = "role_permission?roleId=" + rows[0].roleId;
});

//给角色添加游戏权限
$("#roleGame-btn").on('click', function () {
  var rows = $("#roleTable").bootstrapTable('getSelections');
  if (rows.length <= 0 || rows.length > 1) {
    alert("请选择一条角色信息");
    return;
  }
  //接下来要执行将选中的id作为参数传递到后台
  location.href = "role_game?roleId=" + rows[0].roleId;
});

//给角色添加数据权限
$("#roleData-btn").on('click', function () {
  var rows = $("#roleTable").bootstrapTable('getSelections');
  if (rows.length <= 0 || rows.length > 1) {
    alert("请选择一条角色信息");
    return;
  }
  //接下来要执行将选中的id作为参数传递到后台
  location.href = "role_data?roleId=" + rows[0].roleId;
});
