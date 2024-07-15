function toggleDialog(showed) {
  if (showed) {
    $('.admin-mask').show();
    $('.web-box').show();
  } else {
    $('.admin-mask').hide();
    $('.web-box').hide();
  }
}

function toggleDialog2(showed) {
  if (showed) {
    $('.admin-mask2').show();
    $('.web-box2').show();
  } else {
    $('.admin-mask2').hide();
    $('.web-box2').hide();
  }
}

function formatNull(value) {
  if (value == null) {
    return ''
  } else {
    return value
  }
}

function formatColumn(value) {
  if (value == null || value == '') {
    return ''
  }
  var span = document.createElement('span');
  span.setAttribute('title', value);
  span.innerHTML = value;
  return span.outerHTML;
}

function setEnable(id, enabled) {
  $(id).prop('disabled', !enabled);
}

function setVisible(id, isVisible) {
  if (isVisible) {
    $(id).show();
  } else {
    $(id).hide();
  }

}

function isBlank(val) {
  return val == null || val == "" || val.trim() == "";
}