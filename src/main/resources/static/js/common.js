/**
 * Created by qy_lu on 2018/3/29.
 */
//展示Loading
function g_showLoading() {
    var idx = layer.msg('处理中...', {icon: 16, shade: [0.5, '#f5f5f5'], scrollbar: false, offset: '0px', time: 10000});
    return idx;
}

// salt
var g_password_salt = "1a2b3c4d";