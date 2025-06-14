-- データベース作成
CREATE DATABASE abc_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE abc_system;

-- 社員情報を管理するテーブル
CREATE TABLE staff (
  staff_id VARCHAR(10) PRIMARY KEY, -- 社員ID
  name VARCHAR(50),                 -- 氏名
  furigana VARCHAR(50),             -- フリガナ
  birth_date DATE,                  -- 生年月日
  address VARCHAR(255),             -- 住所
  hire_date DATE,                   -- 入社日
  department VARCHAR(50),           -- 部署
  position VARCHAR(50)              -- 役職
);

-- 社員データを登録
INSERT INTO staff (staff_id, name, furigana, birth_date, address, hire_date, department, position) VALUES
('S0001', '石井 和也', 'イシイ カズヤ', '1966-05-04', '愛知県岡崎市3丁目4-12', '2015-10-29', '営業部', '部長'),
('S0006', '佐藤 和也', 'サトウ カズヤ', '2002-03-28', '愛知県豊橋市3丁目9-9', '2024-06-30', '営業部', '主任');

-- 出張手当のルールを定義するテーブル
CREATE TABLE trip_allowance_rule (
  id INT PRIMARY KEY AUTO_INCREMENT,
  trip_type VARCHAR(20) NOT NULL COMMENT '出張の種類（例：短期出張 / 長期出張 / 研修出張）',
  region_type VARCHAR(50) NOT NULL COMMENT '地域区分（例：物価高水準地域 / 上記以外 / 会社施設・縁故先宿泊）',
  rank_type VARCHAR(20) NOT NULL COMMENT '等級・役職（例：管理 / 一般社員 / 全社員 / 管理職）',
  allowance INT NOT NULL COMMENT '日当（円）',
  hotel_fee VARCHAR(20) NOT NULL COMMENT '宿泊費（例：10000 / 会社にて準備 / 0円）',
  UNIQUE KEY uniq_rule (trip_type, region_type, rank_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 出張手当ルールを登録
-- 短期出張
INSERT INTO trip_allowance_rule (trip_type, region_type, rank_type, allowance, hotel_fee) VALUES
('短期出張', '物価高水準地域', '管理', 4000, '10000'),
('短期出張', '物価高水準地域', '一般社員', 2200, '10000'),
('短期出張', '上記以外', '管理', 4000, '8000'),
('短期出張', '上記以外', '一般社員', 2200, '8000'),
('短期出張', '会社施設・縁故先宿泊', '管理', 5000, '0'),
('短期出張', '会社施設・縁故先宿泊', '一般社員', 5000, '0'),
('短期出張', '上記以外', '管理職', 4000, '8000');

-- 長期出張
INSERT INTO trip_allowance_rule (trip_type, region_type, rank_type, allowance, hotel_fee) VALUES
('長期出張', '物価高水準地域', '管理', 1000, '会社にて準備'),
('長期出張', '物価高水準地域', '一般社員', 1000, '会社にて準備'),
('長期出張', '上記以外', '管理', 1000, '会社にて準備'),
('長期出張', '上記以外', '一般社員', 1000, '会社にて準備'),
('長期出張', '会社施設・縁故先宿泊', '管理', 3500, '0'),
('長期出張', '会社施設・縁故先宿泊', '一般社員', 3500, '0');

-- 研修出張（地域・等級の区別なし）
INSERT INTO trip_allowance_rule (trip_type, region_type, rank_type, allowance, hotel_fee) VALUES
('研修出張', '全国共通', '全社員', 1000, '短期出張同様');

-- 出張費情報を管理するテーブル（申請データ）
CREATE TABLE business_trip_expense (
  id INT AUTO_INCREMENT PRIMARY KEY,
  staff_id VARCHAR(10) NOT NULL,               -- 社員ID（外部参照）
  trip_type VARCHAR(20),                       -- 出張の種類
  project VARCHAR(100),                        -- プロジェクト名
  start_date DATE,                             -- 出張開始日
  end_date DATE,                               -- 出張終了日
  days INT,                                    -- 出張日数
  region_type VARCHAR(50),                     -- 地域区分
  rank_type VARCHAR(20),                       -- 等級
  burden_type VARCHAR(10),                     -- 負担区分（会社/個人）
  daily_allowance INT,                         -- 日当
  hotel_fee INT,                               -- 宿泊費（1泊あたり）
  night_fee INT,                               -- 宿泊費合計
  transport_fee INT,                           -- 交通費
  total_amount INT,                            -- 合計金額
  memo TEXT,                                   -- 備考
  receipt_file VARCHAR(255),                   -- 領収書ファイル名
  status VARCHAR(20) DEFAULT '申請中',         -- ステータス（申請中、承認済など）
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
