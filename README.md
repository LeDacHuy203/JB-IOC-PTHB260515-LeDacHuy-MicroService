# Task 3: Phân tích kiến trúc

## 1. Service Discovery là gì? Vì sao Gateway không gọi thẳng IP và port?

Service Discovery giúp các service tìm được địa chỉ của nhau mà không cần ghi sẵn IP và port trong code.
Khi Student Service khởi động, service sẽ đăng ký với Eureka. 
Gateway dựa vào tên student-service để tìm địa chỉ và chuyển request đến service đó.

Không nên gọi thẳng bằng IP và port vì địa chỉ của service có thể thay đổi khi khởi động lại 
hoặc triển khai lên môi trường khác. Ngoài ra, khi chạy nhiều instance để chia tải, 
Gateway cũng cần biết các instance nào đang hoạt động để chọn nơi gửi request. 
Dùng Eureka giúp việc này linh hoạt hơn và không phải sửa cấu hình Gateway mỗi khi địa chỉ service thay đổi.

## 2. Làm sao scale Student Service khi request tăng?

Khi có nhiều request hơn, có thể chạy thêm các instance của Student Service.
Các instance mới sẽ đăng ký với Eureka, sau đó Gateway dùng load balancer để phân phối request đến chúng.
Vì Gateway gọi service bằng tên, chẳng hạn lb://student-service, 
nên không cần sửa route mỗi lần tăng số instance.

Nếu dùng Kubernetes thì có thể tăng số replicas hoặc cấu hình Horizontal Pod Autoscaler để tự tăng, giảm instance theo tải.
Cũng cần theo dõi database vì nếu database quá tải thì chỉ tăng số instance của Student Service vẫn chưa giải quyết được vấn đề.

## 3. So sánh OpenFeign và Kafka

OpenFeign gọi trực tiếp qua HTTP và chờ phản hồi, phù hợp khi cần kết quả ngay như kiểm tra khóa học còn chỗ. 

Kafka gửi sự kiện để service khác xử lý sau, phù hợp với các tác vụ không cần phản hồi tức thì. 

Vì vậy, bài toán đăng ký khóa học nên dùng OpenFeign để kiểm tra capacity trước khi xác nhận.
