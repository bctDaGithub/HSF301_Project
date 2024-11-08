// Bản đồ ánh xạ các loại tài nguyên với tên hiển thị
const resourceNames = {
    flowers: "Chi tiết hoa",
    order: "Chi tiết đơn hàng",
    cartItem: "Giỏ hàng"
};

export function updateBreadcrumb() {
    const breadcrumbNav = document.getElementById('breadcrumb-nav');
    const historyLinks = JSON.parse(localStorage.getItem('historyLinks')) || [];

    // Kiểm tra nếu trang hiện tại là trang chủ thì không hiển thị breadcrumb và xóa lịch sử
    if (window.location.pathname === '/showHomePage' || window.location.pathname === '/') {
        breadcrumbNav.innerHTML = ''; // Không hiển thị breadcrumb trên trang chủ
        localStorage.removeItem('historyLinks'); // Xóa toàn bộ lịch sử khi về trang chủ
        return;
    }

    // Khởi tạo breadcrumb với liên kết đến Trang chủ
    breadcrumbNav.innerHTML = '<a href="/showHomePage">Trang chủ</a> / ';

    // Hiển thị các trang khác trong lịch sử, tránh trùng lặp
    historyLinks.forEach((link, index) => {
        const separator = index === historyLinks.length - 1 ? '' : ' / ';
        breadcrumbNav.innerHTML += `<a href="${link.url}">${link.displayName}</a>${separator}`;
    });
}

export function saveCurrentPage(name) {
    let currentPage = { url: window.location.href, displayName: name };
    let historyLinks = JSON.parse(localStorage.getItem('historyLinks')) || [];

    // Nếu là trang chủ, xóa toàn bộ lịch sử và dừng hàm
    if (window.location.pathname === '/showHomePage' || window.location.pathname === '/') {
        localStorage.removeItem('historyLinks');
        return;
    }

    // Nhận diện các URL có cấu trúc "/[loai]/[id]"
    const match = window.location.pathname.match(/^\/(\w+)\/\d+$/);
    if (match) {
        const resourceType = match[1]; // Lấy "loai" từ URL, ví dụ "flowers", "order", "cartItem"
        const displayName = resourceNames[resourceType] || `Chi tiết ${resourceType}`;

        // Lưu URL đầy đủ bao gồm ID nhưng sử dụng tên hiển thị chung
        currentPage = { url: window.location.href, displayName: displayName };

        // Kiểm tra nếu đã có một mục "Chi tiết hoa" trong lịch sử
        const existingIndex = historyLinks.findIndex(link => link.displayName === displayName);
        if (existingIndex !== -1) {
            // Nếu đã có, thay thế mục cũ bằng mục mới (URL mới)
            historyLinks = historyLinks.slice(0, existingIndex).concat(currentPage);
        } else {
            // Nếu chưa có, thêm mục mới
            historyLinks.push(currentPage);
        }
    } else {
        // Kiểm tra nếu URL hiện tại đã tồn tại là mục cuối cùng trong historyLinks
        const lastLink = historyLinks[historyLinks.length - 1];
        if (lastLink && lastLink.url === currentPage.url) {
            return; // Nếu trùng, không lưu lại nữa
        }
        // Thêm trang hiện tại vào `historyLinks`
        historyLinks.push(currentPage);
    }

    // Cập nhật lại historyLinks trong localStorage
    localStorage.setItem('historyLinks', JSON.stringify(historyLinks));
}
