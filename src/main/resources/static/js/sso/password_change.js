$("#btn-sure-add").click(function () {

  var bootstrapValidator = $('#passwordChangeForm').data(
      'bootstrapValidator');
  if (bootstrapValidator != null) {
    bootstrapValidator.destroy(true);
  }

  $('#passwordChangeForm').bootstrapValidator({
    message: 'This value is not valid',
    excluded: [':disabled'],
    feedbackIcons: {
      valid: 'glyphicon glyphicon-ok',
      invalid: 'glyphicon glyphicon-remove',
      validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
      oldPassword: {
        validators: {
          notEmpty: {
            message: '旧密码不能为空'
          }
        }
      },
      newPassword: {
        validators: {
          notEmpty: {
            message: '新密码不能为空'
          }
        }
      },
      confirmPassword: {
        validators: {
          notEmpty: {
            message: '确认密码不能为空'
          },
          identical: {
            field: 'newPassword',
            message: '输入的内容不一致'
          }
        }
      }
    }
  });

  bootstrapValidator = $('#passwordChangeForm').data(
      'bootstrapValidator');

  bootstrapValidator.validate();//提交验证

  if (!bootstrapValidator.isValid()) {
    return;
  }

  if (!confirm("确认要修改密码吗？")) {
    return;
  }

  var oldPassword = $("#oldPassword").val();
  var newPassword = $("#newPassword").val();

  var passwordChangeUrl = $('#passwordChangeUrl').attr('href');

  var requestData = {
    'old_password': oldPassword,
    'new_password': newPassword
  };

  $.ajax({
    type: 'POST',
    url: passwordChangeUrl,
    contentType: 'application/json; charset=utf-8',
    dataType: 'json',
    data: JSON.stringify(requestData),
    success: function (data) {
      if (data.result == "ok") {
        alert("修改成功,点击确定跳转到登录界面!");
        window.location.href = "/logout";
      } else if (data.result == "fail") {
        alert("修改失败!")
      } else if (data.result == "wrong_old") {
        alert("旧密码错误!")
      }
    },
    error: function (data) {
      alert("修改失败!")
    }
  })
})
