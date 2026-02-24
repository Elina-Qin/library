// 切换标签页功能
document.getElementById('login-tab').addEventListener('click', function() {
    switchToLogin();
});

document.getElementById('register-tab').addEventListener('click', function() {
    switchToRegister();
});

document.getElementById('switch-to-register').addEventListener('click', function(e) {
    e.preventDefault();
    switchToRegister();
});

document.getElementById('switch-to-login').addEventListener('click', function(e) {
    e.preventDefault();
    switchToLogin();
});

function switchToLogin() {
    document.getElementById('login-tab').classList.add('active');
    document.getElementById('register-tab').classList.remove('active');
    document.getElementById('login-form').classList.add('active');
    document.getElementById('register-form').classList.remove('active');
}

function switchToRegister() {
    document.getElementById('register-tab').classList.add('active');
    document.getElementById('login-tab').classList.remove('active');
    document.getElementById('register-form').classList.add('active');
    document.getElementById('login-form').classList.remove('active');
}

// 为登录表单添加提交事件监听器
document.getElementById('login-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;

    // 发送登录请求
    fetch('/api/user/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({username, password})
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                alert('登录成功: ' + username);
                window.location.href = 'books.html';
            } else {
                alert('登录失败: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('登录请求失败');
        });
});

// 为注册表单添加提交事件监听器
document.getElementById('register-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const username = document.getElementById('register-username').value;
    const password = document.getElementById('register-password').value;
    const confirm = document.getElementById('register-confirm').value;

    if (password !== confirm) {
        alert('两次输入的密码不一致');
        return;
    }

    // 发送注册请求
    fetch('/api/user/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({username, password, confirmPassword: confirm})
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                alert('注册成功，请登录');
                switchToLogin();
            } else {
                alert('注册失败: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('注册请求失败');
        });
});