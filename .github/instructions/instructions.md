Mục tiêu: Xây dựng project Java OOP (không dùng database).
Cấm: Không dùng java.util.List/ArrayList và cũng không dùng Set/HashSet/Map/HashMap hay các implementation tương tự.
Lưu trữ dữ liệu: Dùng mảng (Type[]) hoặc tự triển khai cấu trúc dữ liệu (dynamic array, linked list, hoặc hashtable do bạn viết). Có thể lưu dữ liệu ra file (CSV/JSON/serialization) để persistent.
Thiết kế: Triển khai các lớp theo sơ đồ bạn cung cấp (Person + subclasses; Appointment; Product; Service; Invoice; Payment; Promotion; GoodsReceipt; interfaces: IEntity, IDataManager, IActionManager; enums).
DataStore<T>: thực thi bằng T[] list với API gợi ý: add, update(id), delete(id) (soft-delete ok), findById, findByCondition, getAll, sort, count.
Quan hệ object: lưu tham chiếu trực tiếp hoặc lưu id rồi tìm bằng DataStore.findById; không dùng Map để ánh xạ.
Phân tách: Tách service/business logic khỏi UI/IO; AuthService dạng singleton nếu cần.
Yêu cầu chức năng: CRUD, tìm kiếm, sắp xếp, tính toán, validate input, xử lý lỗi.
Chạy: build bằng Maven (mvn clean package), chạy bằng exec hoặc java -jar.
Tiêu chí chấm điểm: thiết kế OOP, chức năng, xử lý lỗi, code style, tests; +5% nếu implement đúng class diagram và giải thích quan hệ.
