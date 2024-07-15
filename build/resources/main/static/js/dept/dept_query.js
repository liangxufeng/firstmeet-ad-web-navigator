// 部门管理的js文件
$(document).ready(function () {
  initTree();

  // 添加根部门按钮
  $('#addRoot-btn').on('click', function () {
    $("#admin-title").text("添加根级部门");
    $("#deptId").val('');//把隐藏域id置空
    $("#deptParentId").val('0');//把上级id置为0
    $('#deptForm').bootstrapValidator('destroy', true);//把校验数据清空
    $("#deptForm")[0].reset();//每次增加先把表单数据置空
    setEnable("#deptId", true);
    toggleDialog(true);
  });

  // 关闭遮罩层按钮
  $('.websetContClose').on('click', function () {
    toggleDialog(false);
  })
});

/*=====================zTree开始=========================*/

var setting = {
  view: {
    addHoverDom: addHoverDom, //新增
    removeHoverDom: removeHoverDom,
    selectedMulti: false
  },
  edit: {
    enable: true,
    editNameSelectAll: true
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
    beforeDrag: beforeDrag,
    beforeEditName: beforeEditName,
    beforeRemove: beforeRemove,
    beforeRename: beforeRename,
    onRemove: onRemove,
    onRename: onRename,
    beforeClick: beforeClick
  }
};

function beforeClick(treeId, treeNode) {
  var zTree = $.fn.zTree.getZTreeObj("treeDemo");
  zTree.selectNode(treeNode);
  var parentId = treeNode.deptId;
  $("#deptParentId").val(parentId);
}

var log, className = "dark";

function beforeDrag(treeId, treeNodes) {
  return false;
}

function beforeEditName(treeId, treeNode) {
  className = (className === "dark" ? "" : "dark");
  showLog("[ " + getTime() + " beforeEditName ]&nbsp;&nbsp;&nbsp;&nbsp; "
      + treeNode.name);
  var zTree = $.fn.zTree.getZTreeObj("treeDemo");
  zTree.selectNode(treeNode);
  setTimeout(function () {
    // zTree.editName(treeNode);
    editDept(treeNode.deptId);
  }, 0);
  return false;
}

function beforeRemove(treeId, treeNode) {
  className = (className === "dark" ? "" : "dark");
  showLog("[ " + getTime() + " beforeRemove ]&nbsp;&nbsp;&nbsp;&nbsp; "
      + treeNode.name);
  var zTree = $.fn.zTree.getZTreeObj("treeDemo");
  zTree.selectNode(treeNode);
  return confirm("确认删除 节点 -- " + treeNode.deptName + " 吗？");
}

function onRemove(e, treeId, treeNode) {
  showLog("[ " + getTime() + " onRemove ]&nbsp;&nbsp;&nbsp;&nbsp; "
      + treeNode.deptName);
  deleteDept(treeNode.deptId);
}

function beforeRename(treeId, treeNode, newName, isCancel) {
  className = (className === "dark" ? "" : "dark");
  showLog((isCancel ? "<span style='color:red'>" : "") + "[ " + getTime()
      + " beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel
          ? "</span>" : ""));
  if (newName.length == 0) {
    setTimeout(function () {
      var zTree = $.fn.zTree.getZTreeObj("treeDemo");
      zTree.cancelEditName();
      alert("节点名称不能为空.");
    }, 0);
    return false;
  }
  return true;
}

function onRename(e, treeId, treeNode, isCancel) {
  showLog((isCancel ? "<span style='color:red'>" : "") + "[ " + getTime()
      + " onRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel
          ? "</span>" : ""));
}

function showLog(str) {
  if (!log) {
    log = $("#log");
  }
  log.append("<li class='" + className + "'>" + str + "</li>");
  if (log.children("li").length > 8) {
    log.get(0).removeChild(log.children("li")[0]);
  }
}

function getTime() {
  var now = new Date(),
      h = now.getHours(),
      m = now.getMinutes(),
      s = now.getSeconds(),
      ms = now.getMilliseconds();
  return (h + ":" + m + ":" + s + " " + ms);
}

function addHoverDom(treeId, treeNode) {
  var sObj = $("#" + treeNode.tId + "_span");
  if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) {
    return;
  }
  var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
      + "' title='add node' onfocus='this.blur();'></span>";
  sObj.after(addStr);
  var btn = $("#addBtn_" + treeNode.tId);
  if (btn) {
    btn.bind("click", function () {
      //添加部门发送ajax
      /*=============================*/
      $("#admin-title").text("添加部门");
      $("#deptId").val('');//把隐藏域id置空
      $("#longId").val('');//把隐藏域id置空
      $('#deptForm').bootstrapValidator('destroy', true);//把校验数据清空
      $("#deptForm")[0].reset();//每次增加先把表单数据置空
      setEnable("#deptId", true);
      toggleDialog(true);
      /*==============================*/

    });
  }
};

function removeHoverDom(treeId, treeNode) {
  $("#addBtn_" + treeNode.tId).unbind().remove();
};

function selectAll() {
  var zTree = $.fn.zTree.getZTreeObj("treeDemo");
  zTree.setting.edit.editNameSelectAll = $("#selectAll").attr("checked");
}

var initTree = function () {
  var getUserUrl = $('#getDeptUrl').attr('href');
  $.ajax({
    url: getUserUrl,
    method: "GET",
    dataType: "json",
    success: function (data) {
      if (!isEditDept) {
        setting.edit.enable = false;
      }
      if (!isAddDept) {
        setting.view.addHoverDom = null;
      }
      var tree = $.fn.zTree.init($("#treeDemo"), setting, data);
      var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
      var nodes = treeObj.getNodes();
      if (nodes.length > 0) {
        for (var i = 0; i < nodes.length; i++) {
          treeObj.expandNode(nodes[i], true, false, false);//默认展开第一级节点
        }
      }
      $("#selectAll").bind("click", selectAll);
    },
    error: function () {
      alert("加载失败");
    }
  });
};

/*====================zTree结束============================*/

$('#btn-sure-close').on('click', function () {
  toggleDialog(false);
});

$("#btn-sure-add").click(function () {
  $('#deptForm').bootstrapValidator({
    message: 'This value is not valid',
    feedbackIcons: {
      valid: 'glyphicon glyphicon-ok',
      invalid: 'glyphicon glyphicon-remove',
      validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
      deptName: {
        validators: {
          notEmpty: {
            message: '部门名称不能为空'
          }
        }
      },
      deptId: {
        validators: {
          notEmpty: {
            message: '部门ID不能为空'
          }
        }
      },
      deptOrderNum: {
        validators: {
          notEmpty: {
            message: '部门序号不能为空'
          },
          digits: {
            message: '部门序号必须是正整数'
          }
        }
      }
    }
  });
  if (!$('#deptForm').data('bootstrapValidator').isValid()) {
    return;
  }

  var longId = $("#longId").val();
  var deptParentId = $("#deptParentId").val();
  var deptName = $("#deptName").val();
  var deptId = $("#deptId").val();
  var deptOrderNum = $("#deptOrderNum").val();
  var saveDeptUrl = $('#saveDeptUrl').attr('href');

  var zTree = $.fn.zTree.getZTreeObj("treeDemo");
  var treeNode = zTree.getSelectedNodes()[0];
  $.ajax({
    type: 'POST',
    url: saveDeptUrl,
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    dataType: 'json',
    data: {
      'dept_id': deptId,
      'dept_name': deptName,
      'dept_parent_id': deptParentId,
      'dept_orderNum': deptOrderNum,
      'id': longId
    },
    success: function (data) {
      if (data.status == "ok") {
        alert("保存成功");
        if (longId === "") {
          zTree.addNodes(treeNode,
              {deptId: deptId, deptParentId: deptParentId, deptName: deptName});
        } else {
          var node = zTree.getNodeByParam("deptId", deptId, null);
          node.deptName = deptName;
          zTree.updateNode(node);
        }
        toggleDialog(false);
      } else if (data.status == "addIdFail") {
        alert("该部门ID已经存在！")
      } else {
        alert("保存失败，请联系技术")
      }
    },
    error: function (data) {
      alert("保存失败")
    }
  })
})

function deleteDept(deptId) {
  var deleteDeptUrl = $('#deleteDeptUrl').attr('href');
  $.ajax({
    type: 'POST',
    url: deleteDeptUrl,
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    dataType: 'json',
    data: {
      'dept_id': deptId.toString()
    },
    success: function (data) {
      if (data.status == "ok") {
        alert("删除成功");
        $("#deptTable").bootstrapTable('refresh');
      } else {
        alert("删除失败");
      }
    },
    error: function (data) {
      alert("删除失败");
    }
  })
}

function editDept(deptId) {
  var editDeptUrl = $('#editDeptUrl').attr('href');
  $("#admin-title").text("编辑部门");
  $.ajax({
    type: 'GET',
    url: editDeptUrl,
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    dataType: 'json',
    data: {
      'dept_id': deptId
    },
    success: function (data) {
      if (data != null) {
        $("#deptName").val(data.deptName);
        $("#deptParentId").val(data.deptParentId);
        $("#deptId").val(data.deptId);
        $("#deptOrderNum").val(data.deptOrderNum);
        $("#longId").val(data.id);
      }
      $('#deptForm').bootstrapValidator('destroy', true);//把校验数据清空

      setEnable("#deptId", false);
      toggleDialog(true);
    },
    error: function (data) {
    }
  })
}



