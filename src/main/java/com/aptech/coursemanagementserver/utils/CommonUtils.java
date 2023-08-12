package com.aptech.coursemanagementserver.utils;

import java.util.Random;

public class CommonUtils {
    private static final String[] FIRST_NAMES = {
            "An", "Bình", "Cường", "Đức", "Hải", "Hiếu", "Hoàng", "Hồng", "Huyền", "Khánh", "Kiên", "Lan", "Linh",
            "Long", "Mai", "Minh", "Nam", "Ngân", "Ngọc", "Nhật", "Phương", "Quang", "Quyền", "Sơn", "Thành", "Thiên",
            "Thu", "Thư", "Thúy", "Trang", "Trung", "Tùng", "Tuyết", "Việt", "Xuân"
    };

    private static final String[] LAST_NAMES = {
            "Bùi", "Đặng", "Đinh", "Đoàn", "Hà", "Hồ", "Hoàng", "Huỳnh", "Lê", "Lý", "Mai", "Ngô", "Nguyễn", "Phạm",
            "Phan", "Tạ", "Thái", "Trần", "Võ"
    };

    private static final String[] ARTIFICIAL_INTELLIGENCE = {
            "Natural Language Processing", "Computer Vision Fundamentals", "Reinforcement Learning",
            "Deep Learning Fundamentals", "Machine Learning with Python"
    };

    private static final String[] DATA_SCIENCE = {
            "Big Data Analytics", "Time Series Analysis and Forecasting", "Data Visualization with Tableau",
            "Data Mining and Analysis", "Statistical Inference and Modeling"
    };

    private static final String[] GRAPHIC_DESIGN = {
            "Typography and Layout Design", "Digital Illustration Techniques", "Branding and Logo Design",
            "Web Design Fundamentals"
    };

    public static String randomFirstName() {
        Random random = new Random();
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        return firstName;
    }

    public static String randomLastName() {
        Random random = new Random();
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        return lastName;
    }
}
