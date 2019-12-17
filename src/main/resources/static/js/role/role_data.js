// 角色管理的js文件
$(document).ready(function () {

  initTree();

  // 关闭遮罩层按钮
  $('.websetContClose').on('click', function () {
  })

  //重置按钮
  $('#reset').on('click', function () {
    location.reload();
  })

})

var setting = {
  check: {
    enable: true,
    chkStyle: "checkbox",
    chkboxType: {"Y": "ps", "N": "s"}
  },
  data: {
    key: {
      name: "name"
    },
    simpleData: {
      enable: true,
      idKey: "id",
      pIdKey: "pid",
      rootPId: "0"
    }
  }
};

var initTree = function () {
  var getDataWithRoleIdUrl = $('#getDataWithRoleIdUrl').attr('href');
  var role_Id = document.getElementById('roleId').innerHTML;
  var roleId = role_Id.toString();
  $.ajax({
    data: {
      'role_id': roleId
    },
    url: getDataWithRoleIdUrl,
    method: "GET",
    dataType: "json",
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    success: function (data) {
      $.fn.zTree.init($("#treeDemo"), setting, data);
    },
    error: function () {
      alert("加载失败");
    }
  });
};


$("#change-sure-btn").click(function () {
  var roleId = document.getElementById('roleId').innerHTML;
  if (confirm("确认更改用户数据权限吗？")) {
    var teamIdArray = CheckTeamId();
    var productIdArray = CheckProductId();
    var mediaIdArray = CheckMediaId();
    var mediaresourceIdArray = CheckMediaResourceId();
    var changeRoleDataUrl = $('#changeRoleDataUrl').attr('href');
    $.ajax({
      type: 'POST',
      url: changeRoleDataUrl,
      contentType: 'application/x-www-form-urlencoded; charset=utf-8',
      dataType: 'json',
      data: {
        'team_ids': teamIdArray,
        'product_ids': productIdArray,
        'media_ids': mediaIdArray,
        'mediaresource_ids': mediaresourceIdArray,
        'role_id': roleId
      },
      success: function (data) {
        if (data.status == "ok") {
          alert("保存成功");
          location.reload();
        } else {
          alert("保存失败，请联系技术")
          location.reload();
        }
      },
      error: function (data) {
        alert("保存失败")
      }
    })
  }
});

function CheckTeamId() {
  var teamIdArray = new Array()
  var Tabobj = $("#teamTable");
  var Check = $("table input[type=checkbox]:checked");//在table中找input下类型为checkbox属性为选中状态的数据
  Check.each(function () {//遍历
    var row = $(this).parent("td").parent("tr");//获取选中行
    var id = row.find("[name='teamId']").html();//获取name='gameId'的值
    teamIdArray.push(id);
  });
  return teamIdArray;
}

function CheckProductId() {
  var productIdArray = new Array()
  var Tabobj = $("#productTable");
  var Check = $("table input[type=checkbox]:checked");//在table中找input下类型为checkbox属性为选中状态的数据
  Check.each(function () {//遍历
    var row = $(this).parent("td").parent("tr");//获取选中行
    var id = row.find("[name='productId']").html();//获取name='gameId'的值
    productIdArray.push(id);
  });
  return productIdArray;
}

function CheckMediaId(){
  var mediaIdArray = new Array()
  var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
  var nodes = treeObj.getCheckedNodes(true);
  if (nodes.length > 0) {
    for (var i = 0; i < nodes.length; i++) {
      if (nodes[i].pid==0){
        mediaIdArray.push(nodes[i].id);
      }
    }
  }
  return mediaIdArray;
}

function CheckMediaResourceId(){
  var mediaResourceIdArray = new Array();
  var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
  var nodes = treeObj.getCheckedNodes(true);
  if (nodes.length > 0) {
    for (var i = 0; i < nodes.length; i++) {
      if (nodes[i].pid!=0){
        mediaResourceIdArray.push(nodes[i].id);
      }
    }
  }
  return mediaResourceIdArray;
}




/*function CheckMediaId() {
  var mediaIdArray = new Array()
  var Tabobj = $("#mediaTable");
  var Check = $("table input[type=radio]:checked");//在table中找input下类型为radio属性为选中状态的数据
  Check.each(function () {//遍历
    var row = $(this).parent("td").parent("tr");//获取选中行
    var id = row.find("[name='mediaId']").html();//获取name='gameId'的值
    mediaIdArray.push(id);
  });
  return mediaIdArray;
}

function CheckMediaResourceId() {
  var mediaResourceIdArray = new Array()
  var Tabobj = $("#mediaResourceTable");
  var Check = $("table input[type=checkbox]:checked");//在table中找input下类型为checkbox属性为选中状态的数据
  Check.each(function () {//遍历
    var row = $(this).parent("td").parent("tr");//获取选中行
    var id = row.find("[name='mediaResourceId']").html();//获取name='gameId'的值
    mediaResourceIdArray.push(id);
  });
  return mediaResourceIdArray;
}*/

/*
$('input[type=radio][name=roleMedia]').change(function () {

  /!*var boxs = document.getElementsByName("roleMediaResource");
  for (i = 0; i < boxs.length; i++) {
    if (boxs[i].checked) {
      boxs[i].checked = false
    }
  }

  var myDiv = document.getElementById("right");
  myDiv.style.display ="none";*!/

  //一旦改变  记录数据
  var mediaIdArray = CheckMediaId();
  var mediaId = mediaIdArray[0];
  //发送ajax请求,查询该媒体下的资源并显示
  var getMediaResource = $('#getMediaResource').attr('href');
  var roleId = document.getElementById('roleId').innerHTML;
  $.ajax({
    type: 'GET',
    url: getMediaResource,
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    dataType: 'json',
    data: {
      'media_id': mediaId,
      'role_id': roleId
    },
    success: function (data) {
      if (data.status == "ok") {
        alert("保存成功");
      } else {
        alert("保存失败，请联系技术")
        location.reload();
      }
    },
    error: function (data) {
      alert("保存失败")
    }
  })
});*/
