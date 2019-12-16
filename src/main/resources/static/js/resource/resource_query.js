// 资源管理的js文件
$(document).ready(function () {
  initTree();
  $("#copy-btn").bind("click", copy);
  $("#paste-btn").bind("click", paste);

  // 添加根资源按钮
  $('#addRoot-btn').on('click', function () {
    $("#admin-title").text("添加根级资源");
    $("#resourceId").val('');//把隐藏域id置空
    $("#longId").val('');//把隐藏域id置空
    var obj = $("#resourceParentId");
    obj.attr("value", "0");
    parentId = $("#resourceParentId").val();
    $('#resourceForm').bootstrapValidator('destroy', true);//把校验数据清空
    $("#resourceForm")[0].reset();//每次增加先把表单数据置空
    setEnable("#resourceId", true);
    toggleDialog(true);
  });

  // 关闭遮罩层按钮
  $('.websetContClose').on('click', function () {
    toggleDialog(false);
  })
})

/*=====================zTree开始=========================*/

var setting = {
  view: {
    addHoverDom: addHoverDom,
    removeHoverDom: removeHoverDom,
    selectedMulti: false
  },
  edit: {
    enable: true,
    editNameSelectAll: true
  },
  data: {
    key: {
      name: "resourceName"
    },
    simpleData: {
      enable: true,
      idKey: "id",
      pIdKey: "parentId",
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
  var parentId = treeNode.id;
  $("#resourceParentId").val(parentId);
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
    var obj = $("#resourceParentId");
    obj.attr("value", treeNode.parentId);
    parentId = $("#resourceParentId").val();
    editResource(treeNode.id);
  }, 0);
  return false;
}

function beforeRemove(treeId, treeNode) {
  className = (className === "dark" ? "" : "dark");
  showLog("[ " + getTime() + " beforeRemove ]&nbsp;&nbsp;&nbsp;&nbsp; "
      + treeNode.name);
  var zTree = $.fn.zTree.getZTreeObj("treeDemo");
  zTree.selectNode(treeNode);
  return confirm("确认删除 节点 -- " + treeNode.resourceName + " 吗？");
}

function onRemove(e, treeId, treeNode) {
  showLog("[ " + getTime() + " onRemove ]&nbsp;&nbsp;&nbsp;&nbsp; "
      + treeNode.resourceName);
  deleteResource(treeNode.id);
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

var parentId;

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
      //添加资源发送ajax
      /*=============================*/
      $("#admin-title").text("添加资源");
      var obj = $("#resourceParentId");
      obj.attr("value", treeNode.id);
      parentId = $("#resourceParentId").val();
      $("#resourceId").val('');//把隐藏域id置空
      $("#longId").val('');//把隐藏域id置空
      $('#resourceForm').bootstrapValidator('destroy', true);//把校验数据清空
      $("#resourceForm")[0].reset();//每次增加先把表单数据置空
      setEnable("#resourceId", true);

      toggleDialog(true);
      /*==============================*/

    });
  }
};

function removeHoverDom(treeId, treeNode) {
  $("#addBtn_" + treeNode.tId).unbind().remove();
};

var copyId = null;
var targetId = null;

function copy(e) {
  var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
      nodes = zTree.getSelectedNodes();
  if (nodes.length == 0) {
    alert("请先选择一个节点");
    return;
  }
  var node = nodes[0];
  if (node.resourceType !== "system") {
    alert(" 请选择系统进行复制");
    return;
  }
  copyId = node.id;
  $("#copyId").attr("value", copyId);
}

function paste(e) {
  if (copyId === null) {
    alert("请选择一个节点进行复制");
    return;
  }

  var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
      nodes = zTree.getSelectedNodes();
  var node = nodes[0];

  if (node.resourceType !== "system") {
    alert("粘贴位置为系统或者根目录");
    return;
  }

  if (node.id === copyId) {
    targetId = 0;
    $("#targetId").attr("value", targetId);
  } else {
    targetId = nodes[0].id;
    $("#targetId").attr("value", targetId);
  }

  modalBox.show();
}

var initTree = function () {
  var getUserUrl = $('#getResourceUrl').attr('href');
  $.ajax({
    url: getUserUrl,
    method: "GET",
    dataType: "json",
    success: function (data) {
      if (!isAddResource) {
        setting.view.addHoverDom = null;
      }
      if (!isEditResource) {
        setting.edit.enable = false;
      }
      $.fn.zTree.init($("#treeDemo"), setting, data);
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
  $('#resourceForm').bootstrapValidator({
    message: 'This value is not valid',
    feedbackIcons: {
      valid: 'glyphicon glyphicon-ok',
      invalid: 'glyphicon glyphicon-remove',
      validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
      resourceName: {
        validators: {
          notEmpty: {
            message: '资源名称不能为空'
          }
        }
      },
      resourceId: {
        validators: {
          notEmpty: {
            message: '资源ID不能为空'
          }
        }
      },
      resourceOrderNum: {
        validators: {
          notEmpty: {
            message: '资源序号不能为空'
          },
          digits: {
            message: '资源序号必须是正整数'
          }
        }
      }
    }
  });
  if (!$('#resourceForm').data('bootstrapValidator').isValid()) {
    return;
  }

  var longId = $("#longId").val();
  var resourceParentId = $("#resourceParentId").val();
  var resourceName = $("#resourceName").val();
  var resourceType = $("#resourceType").val();
  var resourceUrl = $("#resourceUrl").val();
  var resourceId = $("#resourceId").val();
  var resourceOrderNum = $("#resourceOrderNum").val();
  var saveResourceUrl = $('#saveResourceUrl').attr('href');

  var zTree = $.fn.zTree.getZTreeObj("treeDemo");
  var treeNode = zTree.getSelectedNodes()[0];

  $.ajax({
    type: 'POST',
    url: saveResourceUrl,
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    dataType: 'json',
    data: {
      'resource_id': resourceId,
      'resource_name': resourceName,
      'resource_url': resourceUrl,
      'resource_type': resourceType,
      'resource_parent_id': parentId,
      'resource_orderNum': resourceOrderNum,
      'id': longId
    },
    success: function (data) {
      if (data.status == "ok") {
        alert("保存成功");
        if (longId === "") {
          if ("system" === resourceType) {
            zTree.addNodes(treeNode,
                {
                  id: data.id,
                  parentId: parentId,
                  resourceName: resourceName,
                  iconSkin: "system"
                });
          }
          if ("menu" === resourceType) {
            zTree.addNodes(treeNode,
                {
                  id: data.id,
                  parentId: parentId,
                  resourceName: resourceName,
                  iconSkin: "menu"
                });
          }
          if ("link" === resourceType) {
            zTree.addNodes(treeNode,
                {
                  id: data.id,
                  parentId: parentId,
                  resourceName: resourceName,
                  iconSkin: "link"
                });
          }
          if ("button" === resourceType) {
            zTree.addNodes(treeNode,
                {
                  id: data.id,
                  parentId: parentId,
                  resourceName: resourceName,
                  iconSkin: "button"
                });
          }
        } else {
          var node = zTree.getNodeByParam("id", longId, null);
          node.resourceName = resourceName;
          zTree.updateNode(node);
        }
        toggleDialog(false);
        $("#resourceTable").bootstrapTable('refresh');
      } else if (data.status == "addIdFail") {
        alert("系统ID已经存在！")
      } else if (data.status == "addRIdFail") {
        alert("该系统中此ID已经存在！")
      } else {
        alert("保存失败，请联系技术")
      }
    },
    error: function (data) {
      alert("保存失败")
    }
  })
})

function deleteResource(id) {
  var deleteResourceUrl = $('#deleteResourceUrl').attr('href');

  $.ajax({
    type: 'POST',
    url: deleteResourceUrl,
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    dataType: 'json',
    data: {
      'id': id
    },
    success: function (data) {
      if (data.status == "ok") {
        alert("删除成功");
        $("#resourceTable").bootstrapTable('refresh');

      } else {
        alert("删除失败");
      }
    },
    error: function (data) {
      alert("删除失败");
    }
  })
}

function editResource(id) {
  var editResourceUrl = $('#editResourceUrl').attr('href');
  $("#admin-title").text("编辑资源");
  $.ajax({
    type: 'GET',
    url: editResourceUrl,
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    dataType: 'json',
    data: {
      'id': id
    },
    success: function (data) {
      if (data != null) {
        $("#resourceName").val(data.resourceName);
        $("#resourceType").val(data.resourceType);
        $("#resourceParentId").val(data.resourceParentId);
        $("#resourceId").val(data.resourceId);
        $("#resourceUrl").val(data.resourceUrl);
        $("#resourceOrderNum").val(data.resourceOrderNum);
        $("#longId").val(data.id);
      }
      $('#resourceForm').bootstrapValidator('destroy', true);//把校验数据清空

      setEnable("#resourceId", false);
      toggleDialog(true);
    },
    error: function (data) {
    }
  })
}

/*===========模态框============*/
/*建立模态框对象*/
var modalBox = {};
/*获取模态框*/
modalBox.modal = document.getElementById("myModal");
/*获得关闭按钮*/
$('#closeBtn').on('click', function () {
  modalBox.close();
});

/*模态框显示*/
modalBox.show = function () {
  console.log(this.modal);
  this.modal.style.display = "block";
}
/*模态框关闭*/
modalBox.close = function () {
  this.modal.style.display = "none";
}

/*模态框初始化*/
modalBox.init = function () {
  var that = this;
  this.triggerBtn.onclick = function () {
    that.show();
  }
  this.closeBtn.onclick = function () {
    that.close();
  }
  this.sureCopy.onclick = function () {
    that.close();
  }
}
//modalBox.init();

/*===========模态框============*/

$('#btn-sure-copy').on('click', function () {

  var changeResourceId = $("#changeResourceId").val();
  var changeResourceUrl = $("#changeResourceUrl").val();
  var changeResourceName = $('#changeResourceName').val();
  if (changeResourceId === "" || changeResourceName === "" || changeResourceUrl
      === "") {
    alert("字段不能为空");
    return;
  }

  var targetId = $("#targetId").val();
  var copyId = $("#copyId").val();
  var copyResourceUrl = $('#copyResourceUrl').attr('href');
  $.ajax({
    type: 'POST',
    url: copyResourceUrl,
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    dataType: 'json',
    data: {
      'copyId': copyId,
      'targetId': targetId,
      'changeResourceId': changeResourceId,
      'changeResourceUrl': changeResourceUrl,
      'changeResourceName': changeResourceName
    },
    success: function (data) {
      if (data.status == "ok") {
        alert("复制成功");
        $("#targetId").val("");
        $("#copyId").val("");
        location.reload();
        $("#copyResourceForm")[0].reset();
      } else if (data.status == "typeFail") {
        alert("只能复制系统")
      } else if (data.status == "idFail") {
        alert("系统ID已存在")
      } else if (data.status == "targetTypeFail") {
        alert("只能粘贴到一级或根级系统之下")
      } else {
        alert("复制失败")
      }
    },
    error: function (data) {
      alert("加载失败")
      location.reload();
    }
  });

});
