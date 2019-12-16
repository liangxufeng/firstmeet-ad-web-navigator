$("#submit-login").on('click', function () {
  $("#account-message").html("");
  $("#password-message").html("");

  var account = $('#account').val();
  if (account.trim().length = 0) {
    $("#account-message").html("请输入账号");
    return;
  }

  var password = $('#password').val();
  if (password.trim().length = 0) {
    $("#password-message").html("请输入密码");
    return;
  }

  var sysId = $('#sys_id').val();

  $.ajax({
    type: 'POST',
    url: '/sso/login',
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    dataType: 'json',
    data: {
      'account': account,
      'password': password
    },
    success: function (data) {
      if (data.status == "ok") {
        if (sysId.trim().length = 0) {
          location.href = "/index";
        } else {
          location.href = "/sso/sys?sys_id=" + sysId;
        }
      } else if (data.status == "pwd_wrong") {
        $("#password-message").html("密码错误");
      } else if (data.status == "user_wrong") {
        $("#account-message").html("账号不存在");
      } else {
        alert("登录失败，请联系技术")
      }
    },
    error: function () {
      alert("加载失败!");
    }
  })
});




