// 游戏管理的js文件
$(document).ready(function () {
  initTable('');

  // 添加游戏按钮
  $('#add-btn').on('click', function () {
    $("#admin-title").text("添加游戏");
    $("#id").val('');//把隐藏域id置空
    $('#gameForm').bootstrapValidator('destroy', true);//把校验数据清空
    $("#gameForm")[0].reset();//每次增加先把表单数据置空
    setEnable("#gameId", true);
    toggleDialog(true);
  });

  //重置按钮
  $('#reset').on('click', function () {
    $('#searchGameName').val('');
    $("#gameTable").bootstrapTable('destroy');
    initTable('')
  });

  // 关闭遮罩层按钮
  $('.websetContClose').on('click', function () {
    toggleDialog(false);
  });

// 模糊查询
  $('#search').on('click', function () {
    var gameName = $("#searchGameName").val();
    $("#gameTable").bootstrapTable('destroy');
    initTable(gameName);
  })
});

var initTable = function (gameName) {
  var getGameUrl = $('#getGameUrl').attr('href');
  $('#gameTable').bootstrapTable({
    contentType: "application/x-www-form-urlencoded;charset=UTF-8",
    url: getGameUrl,                      //请求后台的URL（*）
    method: 'GET',                      //请求方式（*）
    //toolbar: '#toolbar',              //工具按钮用哪个容器
    striped: true,                      //是否显示行间隔色
    cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
    pagination: true,                   //是否显示分页（*）
    sortable: false,                     //是否启用排序
    sortOrder: "asc",                   //排序方式
    sidePagination: "server",           //分页方式：client游戏分页，server服务端分页（*）
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
        game_name: gameName,
        page_size: params.limit,
        page: (params.offset / params.limit) + 1  //页码
        // sort: params.sort,      //排序列名
        // sortOrder: params.order //排位命令（desc，asc）
      };
      return temp;
    },
    columns: [{
      checkbox: true,
      visible: true                  //是否显示复选框
    }, {
      field: 'id',
      title: '游戏ID'
    }, {
      field: 'name',
      title: '游戏名称'
    },  {
      field: 'code',
      title: 'code'
    }, {
      field: 'icon',
      title: 'icon'
    },  {
      field: 'intro',
      title: '介绍'
    },   {
      field: 'isDel',
      title: '是否启用'
    },  {
      field: 'ctime',
      title: '创建时间'
    }, {
      field: 'utime',
      title: '修改时间'
    }],
    onLoadSuccess: function () {
    },
    responseHandler: function (res) {
      var data = {
        total: res.total_elements,
        rows: res.game
      };
      return data;
    },
    onLoadError: function () {
      // showTips("数据加载失败！");
    },
    onDblClickRow: function (row, $element) {
      var id = row.gameId;
      //editGame(id);
    }
  });
};

$('#btn-sure-close').on('click', function () {
  toggleDialog(false);
});

$("#btn-sure-add").click(function () {

  $('#gameForm').bootstrapValidator({
    message: 'This value is not valid',
    feedbackIcons: {
      valid: 'glyphicon glyphicon-ok',
      invalid: 'glyphicon glyphicon-remove',
      validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
      gameName: {
        validators: {
          notEmpty: {
            message: '游戏名称不能为空'
          }
        }
      },
      gameId: {
        validators: {
          notEmpty: {
            message: '游戏ID不能为空'
          }
        }
      }
    }
  });

  if (!$('#gameForm').data('bootstrapValidator').isValid()) {
    return;
  }
  var id = $("#id").val();
  var gameName = $("#gameName").val();
  var gameId = $("#gameId").val();
  var saveGameUrl = $('#saveGameUrl').attr('href');
  $.ajax({
    type: 'POST',
    url: saveGameUrl,
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    dataType: 'json',
    data: {
      'id': id,
      'game_id': gameId,
      'game_name': gameName
    },
    success: function (data) {
      if (data.status == "ok") {
        alert("保存成功");
        toggleDialog(false);
        $("#gameTable").bootstrapTable('refresh');
      } else if (data.status == "addIdFail") {
        alert("该游戏ID已经存在！")
      } else {
        alert("保存失败，请联系技术")
      }
    },
    error: function (data) {
      alert("保存失败")
    }
  })
});

//删除游戏
$("#del-btn").on('click', function () {
  var rows = $("#gameTable").bootstrapTable('getSelections');
  var gameIdArray = new Array()
  if (rows.length <= 0) {
    alert("请选择至少一行数据删除");
    return;
  }
  if (confirm("确认删除该条数据吗？")) {
    for (var i = 0; rows && i < rows.length; i++) {
      var row = rows[i];
      gameIdArray.push(row.gameId);
    }
    var deleteGameUrl = $('#deleteGameUrl').attr('href');
    $.ajax({
      type: 'POST',
      url: deleteGameUrl,
      contentType: 'application/x-www-form-urlencoded; charset=utf-8',
      dataType: 'json',
      data: {
        'game_ids': gameIdArray
      },
      success: function (data) {
        if (data.status == "ok") {
          alert("删除成功");
          $("#gameTable").bootstrapTable('refresh');
        } else {
          alert("删除失败");
        }
      },
      error: function (data) {
        alert("删除失败");
      }
    })
  }
});

$("#edit-btn").on('click', function () {
  var rows = $("#gameTable").bootstrapTable('getSelections');
  if (rows.length <= 0 || rows.length > 1) {
    alert("请选择一行数据进行编辑");
    return;
  }
  editGame(rows[0].gameId);
});

//编辑client
function editGame(gameId) {
  var editGameUrl = $('#editGameUrl').attr('href');
  $("#admin-title").text("编辑游戏");
  $.ajax({
    type: 'GET',
    url: editGameUrl,
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    dataType: 'json',
    data: {
      'game_id': gameId
    },
    success: function (data) {
      if (data != null) {//数据回显
        $("#gameName").val(data.gameName);
        $("#gameId").val(data.gameId);
        $("#id").val(data.id);
      }
      $('#gameForm').bootstrapValidator('destroy', true);//把校验数据清空
      setEnable("#gameId", false);
      toggleDialog(true);
    },
    error: function (data) {
    }
  })
}

