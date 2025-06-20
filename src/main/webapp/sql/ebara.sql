CREATE TABLE department_master (
    department_id VARCHAR(5) NOT NULL PRIMARY KEY,
    department_name VARCHAR(20)
);

INSERT INTO department_master (department_id, department_name) VALUES
('D001', '管理部'),
('D002', '営業部'),
('D003', '総務部'),
('D004', '経理部'),
('D005', '生育部');



INSERT INTO staff (
  staff_id, password, name, furigana, birth_date, address, hire_date, department, position
) VALUES
-- 部長: 石井 和也
('S1001', 'pass123', '石井 和也', 'イシイ カズヤ', '1966-05-04', '愛知県岡崎市3丁目4-12', '2015-10-29', '管理部', '一般社員');



-- テーブル定義書準拠：会社内で行うプロジェクトの基本情報を管理するテーブル群。								
CREATE TABLE project_info (
    project_code VARCHAR(8) NOT NULL PRIMARY KEY COMMENT '各プロジェクトを識別するコード。主キーとして使用。',
    project_name VARCHAR(20) NULL COMMENT 'プロジェクトの名称',
    project_owner VARCHAR(20) NULL COMMENT 'プロジェクトの主な責任者名',
    start_date DATE NULL COMMENT 'プロジェクトの開始日。',
    end_date DATE NULL COMMENT 'プロジェクトの終了予定日または実施日。'
);

CREATE TABLE project_budget (
    project_code VARCHAR(8) NOT NULL COMMENT '対象プロジェクトの識別コード。プロジェクトマスタと紐づく',
    project_budget INT DEFAULT 0 COMMENT 'プロジェクトに割り当てられた予算額',
    project_actual INT DEFAULT 0 COMMENT '実際に使用された費用',
    PRIMARY KEY (project_code)
);

CREATE TABLE project_management (
    employee_id VARCHAR(5) NOT NULL COMMENT 'プロジェクトに参加する社員のID。社員マスタと連携。',
    project_code VARCHAR(8) COMMENT '担当プロジェクトの識別コード。プロジェクトマスタと連携。',
    role VARCHAR(10) COMMENT 'プロジェクト内での担当。',
    remarks VARCHAR(50) COMMENT '補足説明や自由記述欄。役割や関与内容の補足など。',
    report VARCHAR(200) COMMENT 'プロジェクト業務に関する報告内容。定型ではない自由記述。',
    PRIMARY KEY (employee_id)
);


--(仮)プロジェクト用に使用
INSERT INTO personal_info (employee_id, full_name, furigana, birth_date, address, join_date) VALUES
('E001', '山田 太郎', 'やまだ たろう', '1980-01-15', '東京都千代田区', '2005-04-01'),
('E002', '佐藤 花子', 'さとう はなこ', '1985-07-20', '大阪市北区', '2010-06-15'),
('E003', '鈴木 一郎', 'すずき いちろう', '1990-12-05', '名古屋市中区', '2012-09-30'),
('E004', '高橋 恵美', 'たかはし えみ', '1988-03-22', '福岡市中央区', '2014-11-10'),
('E005', '田中 宏', 'たなか ひろし', '1979-05-10', '札幌市豊平区', '2000-01-20'),
('E006', '伊藤 直樹', 'いとう なおき', '1982-09-14', '仙台市青葉区', '2008-05-20'),
('E007', '渡辺 真理', 'わたなべ まり', '1987-04-07', '広島市中区', '2011-08-01'),
('E008', '松本 健太', 'まつもと けんた', '1992-11-11', '京都市下京区', '2015-03-10'),
('E009', '中村 美咲', 'なかむら みさき', '1991-06-18', '横浜市西区', '2013-07-25');


-- project_info テーブルのサンプルデータ
INSERT INTO project_info (project_code, project_name, project_owner, start_date, end_date) VALUES
('PRJ001', '新商品開発', '田中太郎', '2025-01-10', '2025-06-30'),
('PRJ002', '社内システム刷新', '鈴木花子', '2025-02-01', '2025-12-31'),
('PRJ003', 'マーケティング調査', '佐藤次郎', '2025-03-15', '2025-09-30');


-- project_budget テーブルのサンプルデータ
INSERT INTO project_budget (project_code, project_budget, project_actual) VALUES
('PRJ001', 5000000, 3200000),
('PRJ002', 8000000, 4500000),
('PRJ003', 3000000, 2800000);


-- project_management テーブルのサンプルデータ
-- 複数メンバーが同じプロジェクトに所属するパターンも含む
INSERT INTO project_management (employee_id, project_code, role, remarks, report) VALUES
('E001', 'PRJ001', 'PM', 'プロジェクトマネージャ', '進捗順調'),
('E002', 'PRJ001', 'Dev', 'バックエンド開発', 'API設計完了'),
('E003', 'PRJ001', 'QA', 'テスト担当', 'テストケース作成中'),

('E004', 'PRJ002', 'PM', 'プロジェクトマネージャ', 'スケジュール調整中'),
('E005', 'PRJ002', 'Dev', 'フロントエンド', 'UI設計進行中'),
('E006', 'PRJ002', 'Dev', 'インフラ構築', 'AWS環境構築完了'),

('E007', 'PRJ003', 'PM', 'プロジェクトマネージャ', '調査計画立案中'),
('E008', 'PRJ003', 'Analyst', 'データ分析', 'データ収集中'),
('E009', 'PRJ003', 'Support', 'サポート', '報告書作成準備中');