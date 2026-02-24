// 初始化页面时加载图书列表
document.addEventListener('DOMContentLoaded', function() {
    loadBooks();
});

// 加载图书列表
function loadBooks() {
    fetch('/api/book/list')
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                updateBooksTable(data.data);
            } else {
                alert('获取图书列表失败: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('获取图书列表请求失败');
        });
}

// 显示添加图书模态框
function showAddModal() {
    document.getElementById('add-modal').style.display = 'flex';
}

// 隐藏添加图书模态框
function hideAddModal() {
    document.getElementById('add-modal').style.display = 'none';
}

// 添加图书表单提交处理
document.getElementById('add-book-form').addEventListener('submit', function(e) {
    e.preventDefault();

    const number = document.getElementById('book-id').value;
    const name = document.getElementById('book-name').value;
    const author = document.getElementById('book-author').value;

    fetch('/api/book', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({编号: number, 书名: name, 作者: author})
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                loadBooks(); // 重新加载图书列表
                hideAddModal();
                this.reset();
            } else {
                alert('添加失败: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('添加请求失败');
        });
});

// 显示编辑图书模态框（使用图书编号作为标识）
function showEditModal(number) {
    // 根据图书编号获取图书信息
    fetch(`/api/book/${number}`)
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                const book = data.data;
                // 存储当前编辑的图书编号（用于更新时作为条件）
                document.getElementById('edit-index').value = book.编号;
                // 填充表单
                document.getElementById('edit-book-id').value = book.编号;
                document.getElementById('edit-book-name').value = book.书名;
                document.getElementById('edit-book-author').value = book.作者;
                document.getElementById('edit-modal').style.display = 'flex';
            } else {
                alert('获取图书信息失败: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('获取图书信息请求失败');
        });
}

// 隐藏编辑图书模态框
function hideEditModal() {
    document.getElementById('edit-modal').style.display = 'none';
}

// 编辑图书表单提交处理（使用图书编号作为更新条件）
document.getElementById('edit-book-form').addEventListener('submit', function(e) {
    e.preventDefault();

    // 获取原图书编号（作为更新条件）
    const originalNumber = document.getElementById('edit-index').value;
    // 获取表单中的新值
    const newNumber = document.getElementById('edit-book-id').value;
    const newName = document.getElementById('edit-book-name').value;
    const newAuthor = document.getElementById('edit-book-author').value;

    fetch(`/api/book/${originalNumber}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            编号: newNumber,
            书名: newName,
            作者: newAuthor
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                loadBooks(); // 重新加载图书列表
                hideEditModal();
            } else {
                alert('更新失败: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('更新请求失败');
        });
});

// 删除图书（使用图书编号作为标识）
function deleteBook(number) {
    if (confirm('确定要删除这本书吗？')) {
        fetch(`/api/book/${number}`, {
            method: 'DELETE'
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    loadBooks(); // 重新加载图书列表
                } else {
                    alert('删除失败: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('删除请求失败');
            });
    }
}

// 更新图书表格内容
function updateBooksTable(books) {
    const tbody = document.getElementById('books-list');
    tbody.innerHTML = '';

    // 遍历图书列表，生成表格行
    books.forEach((book) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${book.编号}</td>
            <td>${book.书名}</td>
            <td>${book.作者}</td>
            <td>
                <button class="action-btn edit-btn" onclick="showEditModal('${book.编号}')">编辑</button>
                <button class="action-btn delete-btn" onclick="deleteBook('${book.编号}')">删除</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}
