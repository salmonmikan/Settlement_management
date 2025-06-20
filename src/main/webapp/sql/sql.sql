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
  
<<<<<<< HEAD
-- Yui
  -- 申請一覧画面に表示される共通情報を管理するヘッダーテーブル（全ての申請タイプ共通) 
  CREATE TABLE application_header (
  application_id INT AUTO_INCREMENT PRIMARY KEY,
  staff_id VARCHAR(10) NOT NULL,
  application_type VARCHAR(20) NOT NULL,
  application_date DATETIME NOT NULL,
  amount INT NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT '未提出',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
);

-- Yui
-- 出張費申請の基本情報を保存するテーブル
-- application_headerと1対1で紐づく
-- 出張期間、PJコード、出張報告、合計日数などを格納
CREATE TABLE business_trip_application (
  id INT AUTO_INCREMENT PRIMARY KEY,
  application_id INT NOT NULL,                     -- FK tới application_header
  start_date DATE NOT NULL,                        -- 出張開始日
  end_date DATE NOT NULL,                          -- 出張終了日
  project_code VARCHAR(20) NOT NULL,               -- PJコード
  report TEXT,                                     -- 出張報告（自由記述）
  total_days INT NOT NULL,                         -- 合計日数

  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  FOREIGN KEY (application_id) REFERENCES application_header(application_id)
);

-- Yui
-- 出張費申請の明細情報を保存するテーブル（Step2 / Step3 両方）
-- business_trip_applicationと1対多で紐づく
-- step_type（step2 または step3）で明細タイプを区別
-- 日当・宿泊費、または交通費のブロック単位で1レコード
CREATE TABLE business_trip_detail (
  detail_id INT AUTO_INCREMENT PRIMARY KEY,
  trip_application_id INT NOT NULL,              -- FK tới business_trip_application.id
  step_type ENUM('step2', 'step3') NOT NULL,     -- phân biệt loại block
  -- Step 2 (宿泊・日当)
  region_type VARCHAR(50),                       -- 地域区分
  trip_type VARCHAR(50),                         -- 出徒区分
  hotel VARCHAR(100),                            -- 宿泊先
  burden VARCHAR(20),                            -- 負担者（会社／自己）
  hotel_fee INT,                                 -- 宿泊費
  daily_allowance INT,                           -- 日当
  days INT,                                      -- 日数
  expense_total INT,                             -- 合計
  memo TEXT,                                     -- 摘要

  -- Step 3 (交通費)
  trans_project VARCHAR(50),                     -- 訪問先 (PJコード)
  departure VARCHAR(100),                        -- 出発
  arrival VARCHAR(100),                          -- 到着
  transport VARCHAR(50),                         -- 交通機関
  fare_amount INT,                               -- 金額
  trans_trip_type VARCHAR(50),                   -- 区分（往復など）
  trans_burden VARCHAR(20),                      -- 負担者
  trans_expense_total INT,                       -- 合計
  trans_memo TEXT,                               -- 摘要

  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  FOREIGN KEY (trip_application_id) REFERENCES business_trip_application(id)
);
--Yui
-- 領収書ファイルの情報を保存する共通テーブル 
-- business_trip_detailの各ブロックと1対多で紐づく
-- ref_tableとref_idで関連付け先を動的に識別（拡張性あり）
-- ファイル名、保存パス、アップロード日時などを管理
CREATE TABLE receipt_file (
  receipt_id INT AUTO_INCREMENT PRIMARY KEY,
  ref_table VARCHAR(50) NOT NULL,        -- Tên bảng liên kết (ở đây: luôn là 'business_trip_detail')
  ref_id INT NOT NULL,                   -- ID bản ghi (ở đây: business_trip_detail.detail_id)

  original_file_name VARCHAR(255) NOT NULL,    -- Tên file user upload
  stored_file_path VARCHAR(255) NOT NULL,      -- Đường dẫn thật (VD: /uploads/...)
  uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);




=======
-- 部署管理テーブル_ebara
CREATE TABLE department_master (
    department_id VARCHAR(5) NOT NULL PRIMARY KEY,
    department_name VARCHAR(20)
);

INSERT INTO department_master (department_id, department_name) VALUES
('D001', '営業部'),
('D002', '管理部');
>>>>>>> refs/remotes/origin/develop
