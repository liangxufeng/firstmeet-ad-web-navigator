// 角色授权的js文件
$(document).ready(function () {
  initTree();
  // 关闭遮罩层按钮
  $('.websetContClose').on('click', function () {
    toggleDialog(false);
  })
});

$("#reset").on('click', function () {
  initTree();
});

var setting = {
  check: {
    enable: true,
    chkStyle: "checkbox",
    chkboxType: {"Y": "ps", "N": "s"}
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
  }
};

var initTree = function () {
  var getResourceWithRoleIdUrl = $('#getResourceWithRoleIdUrl').attr('href');
  var role_Id = document.getElementById('roleId').innerHTML;
  var roleId = role_Id.toString();
  $.ajax({
    data: {
      'role_id': roleId
    },
    url: getResourceWithRoleIdUrl,
    method: "GET",
    dataType: "json",
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    success: function (data) {
      $.fn.zTree.init($("#treeDemo"), setting, data);
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
};

$("#change-sure-btn").on('click', function () {
  var changeRoleResourceUrl = $('#changeRoleResourceUrl').attr('href');
  var resourceIdArray = getCheckedId();

  var roleId = document.getElementById('roleId').innerHTML;
  $.ajax({
    data: {
      'resource_ids': resourceIdArray,
      'role_id': roleId
    },
    url: changeRoleResourceUrl,
    method: "POST",
    dataType: "json",
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    success: function (data) {
      if (data.status == "ok") {
        alert("修改成功,用户下次登陆时生效");
        initTree();
      } else {
        data.status == "fail"
        alert("修改失败");
      }
    },
    error: function () {
      alert("加载失败");
    }
  });
});

//获取被选中复选框的资源id
function getCheckedId() {
  var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
  var nodes = treeObj.getCheckedNodes(true);
  var resourceIdArray = new Array();
  if (nodes.length == 0) {
    return resourceIdArray;
  }
  for (var i = 0; i < nodes.length; i++) {
    var id = nodes[i].id;
    resourceIdArray.push(id);
  }
  return resourceIdArray;
}