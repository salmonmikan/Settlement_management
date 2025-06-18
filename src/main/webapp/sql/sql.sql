-- データベース作成
DROP DATABASE abc_system;

CREATE DATABASE abc_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE abc_system;

-- 社員情報を管理するテーブル
CREATE TABLE staff (
  staff_id VARCHAR(10) PRIMARY KEY, -- 社員ID
  password VARCHAR(255), -- password fix ebara
  name VARCHAR(50),                 -- 氏名
  furigana VARCHAR(50),             -- フリガナ
  birth_date DATE,                  -- 生年月日
  address VARCHAR(255),             -- 住所
  hire_date DATE,                   -- 入社日
  department VARCHAR(50),           -- 部署
  position VARCHAR(50)              -- 役職
);

-- 社員データを登録
INSERT INTO staff (
  staff_id, password, name, furigana, birth_date, address, hire_date, department, position
) VALUES
-- 部長: 石井 和也
('S0001', 'pass123', '石井 和也', 'イシイ カズヤ', '1966-05-04', '愛知県岡崎市3丁目4-12', '2015-10-29', '営業部', '部長'),
-- 主任: 佐藤 和也
('S0006', 'pass456', '佐藤 和也', 'サトウ カズヤ', '2002-03-28', '愛知県豊橋市3丁目9-9', '2024-06-30', '営業部', '主任'),
-- 一般社員: 田中 太郎
('S0009', 'pass789', '田中 太郎', 'タナカ タロウ', '1998-07-15','愛知県名古屋市1丁目2-3', '2023-04-01', '営業部', '一般社員'),
('S0002', 'pass234', '山田 太郎', 'ヤマダ タロウ', '2000-05-06', '大阪府大阪市', '2025-04-01', '管理部', '一般社員'),
('S0003', 'pass345', '藤田 太郎', 'フジタ タロウ', '1995-09-06', '大阪府大阪市', '2020-04-01', '管理部', '主任');

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
-- プロジェクト管理テーブル
CREATE TABLE project_manage (
  project_id VARCHAR(20) PRIMARY KEY,
  project_name VARCHAR(100),
  department_name VARCHAR(100),
  remarks TEXT,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO project_manage (project_id, project_name, department_name) VALUES
  ('EDLCE001', 'Delta-college（大阪）', '教育事業部'),
  ('MKHRF004', '研修／資格取得', '管理部'),
  ('MSALY001', '営業全般', '管理部'),
  ('MKNR1001', '管理全般', '管理部'),
  ('MSOMF002', '採用業務', '管理部'),
  ('IISTB001', 'アイスタディ富士電機カスタマイズ', 'ITソリューション部'),
  ('IKSMB005', '島根案件（導入支援）', 'ITソリューション事業部'),
  ('ENBCD001', '（NBCC） SF基礎セミナー', 'カスタマーサポート部'),
  ('CHKSC001', 'ハウキン', 'カスタマーサポート部'),
  ('CTECBC001', '東海ECシステム開発', 'カスタマーサポート部'),
  ('CTZKC001', '都築電気日報管理システム保守サポート', 'カスタマーサポート部'),
  ('ECLAE001', '（FLM）講師派遣', '教育事業部'),
  ('EFULE001', '（フルネス）講師派遣', '教育事業部'),
  ('EHLAE001', '（ロジLA）講師派遣', '教育事業部'),
  ('EHPEE001', '（日本HP）講師派遣', '教育事業部'),
  ('ESEPE001', '（SEプラス）講師派遣', '教育事業部'),
  ('EPROE001', '（ProVision）1社研修', '教育事業部'),
  ('EAIXE001', '（AIX）1社研修', '教育事業部'),
  ('EDNAE001', '（DeNA）1社研修', '教育事業部'),
  ('EKHRE001', '(khronos）新人研修・大阪', '教育事業部'),
  ('EKHRE002', '(khronos）個別研修・大阪', '教育事業部'),
  ('EKHRE101', '(khronos）新人研修・東京', '教育事業部'),
  ('EKHRE102', '(khronos）個別研修・東京', '教育事業部'),
  ('EKHRE201', '(khronos）新人研修・中部', '教育事業部'),
  ('EKHRE202', '(khronos）個別研修・中部', '教育事業部'),
  ('EKHRE004', '(khronos）教育・運営', '教育事業部'),
  ('ESALY001', '営業全般（大阪）', '教育事業部'),
  ('ESALY101', '営業全般（東京）', '教育事業部');
  
-- 部署管理テーブル_ebara
CREATE TABLE department_master (
    department_id VARCHAR(5) NOT NULL PRIMARY KEY,
    department_name VARCHAR(20)
);

INSERT INTO department_master (department_id, department_name) VALUES
('D001', '営業部'),
('D002', '管理部');