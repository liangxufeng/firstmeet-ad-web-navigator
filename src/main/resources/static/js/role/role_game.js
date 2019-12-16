// 角色管理的js文件
$(document).ready(function () {
  // 关闭遮罩层按钮
  $('.websetContClose').on('click', function () {
    toggleDialog(false);
  })
})

$("#change-sure-btn").click(function () {
  var roleId = document.getElementById('roleId').innerHTML;
  if (confirm("确认更改用户角色吗？")) {
    var gameIdArray = Check();
    var changeRoleGameUrl = $('#changeRoleGameUrl').attr('href');
    $.ajax({
      type: 'POST',
      url: changeRoleGameUrl,
      contentType: 'application/x-www-form-urlencoded; charset=utf-8',
      dataType: 'json',
      data: {
        'game_ids': gameIdArray,
        'role_id': roleId
      },
      success: function (data) {
        if (data.status == "ok") {
          alert("保存成功")
          toggleDialog(false);
          $("#gameTable").bootstrapTable('refresh');
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

function Check() {
  var gameIdArray = new Array()
  var Tabobj = $("#gameTable");
  var Check = $("table input[type=checkbox]:checked");//在table中找input下类型为checkbox属性为选中状态的数据
  Check.each(function () {//遍历
    var row = $(this).parent("td").parent("tr");//获取选中行
    var id = row.find("[name='gameId']").html();//获取name='gameId'的值
    gameIdArray.push(id);
  });
  return gameIdArray;
}