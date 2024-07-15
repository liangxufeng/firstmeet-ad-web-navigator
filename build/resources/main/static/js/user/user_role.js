// 角色管理的js文件
$(document).ready(function () {
  // 关闭遮罩层按钮
  $('.websetContClose').on('click', function () {
    toggleDialog(false);
  })
})

$("#change-sure-btn").click(function () {
  var userId = document.getElementById('userId').innerHTML;
  if (confirm("确认更改用户角色吗？")) {
    var roleIdArray = Check();
    var changeUserRoleUrl = $('#changeUserRoleUrl').attr('href');
    $.ajax({
      type: 'POST',
      url: changeUserRoleUrl,
      contentType: 'application/x-www-form-urlencoded; charset=utf-8',
      dataType: 'json',
      data: {
        'role_ids': roleIdArray,
        'user_id': userId
      },
      success: function (data) {
        if (data.status == "ok") {
          alert("保存成功")
          toggleDialog(false);
          $("#roleTable").bootstrapTable('refresh');
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
  var roleIdArray = new Array()
  var Tabobj = $("#roleTable");
  var Check = $("table input[type=checkbox]:checked");//在table中找input下类型为checkbox属性为选中状态的数据
  Check.each(function () {//遍历
    var row = $(this).parent("td").parent("tr");//获取选中行
    var id = row.find("[name='roleId']").html();//获取name='roleId'的值
    roleIdArray.push(id);
  });
  return roleIdArray;
}