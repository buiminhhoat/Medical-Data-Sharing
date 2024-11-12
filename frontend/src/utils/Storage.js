// Storage.js
class Storage {
    // Phương thức để lưu thông tin
    static setItem(key, value) {
      localStorage.setItem(key, JSON.stringify(value));
    }
  
    // Phương thức để lấy thông tin
    static getItem(key) {
      const data = localStorage.getItem(key);
      return data ? JSON.parse(data) : null;
    }
  
    // Phương thức để xóa thông tin
    static removeItem(key) {
      localStorage.removeItem(key);
    }
  
    // Phương thức để lưu thông tin access_token, userId, role
    static saveData({ access_token, userId, role }) {
      this.setItem('access_token', access_token);
      this.setItem('userId', userId);
      this.setItem('role', role);
    }
  
    // Phương thức để lấy tất cả thông tin access_token, userId, role
    static getData() {
      return {
        access_token: this.getItem('access_token'),
        userId: this.getItem('userId'),
        role: this.getItem('role')
      };
    }
  
    // Phương thức để xóa tất cả thông tin access_token, userId, role
    static clearData() {
      this.removeItem('access_token');
      this.removeItem('userId');
      this.removeItem('role');
    }
  }
  
  export default Storage;  