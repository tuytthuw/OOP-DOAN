package enums;

/**
 * Enum định nghĩa các trạng thái nhân viên trong hệ thống.
 * Trạng thái ảnh hưởng đến khả năng làm việc của nhân viên.
 */
public enum EmployeeStatus {
    ACTIVE, // Hoạt động - Nhân viên có thể làm việc bình thường
    ON_LEAVE, // Đang nghỉ phép - Tạm thời không làm việc
    SUSPENDED, // Tạm dừng - Nhân viên bị tạm đình chỉ
    RESIGNED // Đã từ chức - Nhân viên rời khỏi công ty
}
