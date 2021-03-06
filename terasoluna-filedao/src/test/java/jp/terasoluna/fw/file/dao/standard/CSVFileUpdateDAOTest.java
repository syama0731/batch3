/*
 * $Id:$
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import jp.terasoluna.fw.file.dao.FileLineWriter;
import jp.terasoluna.fw.file.ut.VMOUTUtil;
import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.CSVFileUpdateDAO} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> CSVファイル用のFileLineWriterを生成する。<br>
 * AbstractFileUpdateDAOのサブクラス。
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.CSVFileUpdateDAO
 */
public class CSVFileUpdateDAOTest extends TestCase {

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(CSVFileUpdateDAOTest.class);
    }

    /**
     * 初期化処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VMOUTUtil.initialize();
    }

    /**
     * 終了処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * コンストラクタ。
     * @param name このテストケースの名前。
     */
    public CSVFileUpdateDAOTest(String name) {
        super(name);
    }

    /**
     * testExecute01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E.F <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * (引数) clazz:Class<T>インスタンス<br>
     * (状態) AbstractFileUpdateDAO.columnFormatterMap: HashMapインスタンス<br>
     * <br>
     * 期待値：(戻り値) fileLineWriter:CSVFileLineWriter<T>インスタンス<br>
     * (状態変化) CSVFileLineWriter#CSVFileLineWriter():1回呼ばれる。<br>
     * 呼ばれるときの引数を確認する。<br>
     * (状態変化) AbstractFileUpdateDAO#getColumnFormatterMap(): 1回呼ばれる<br>
     * <br>
     * 正常パターン<br>
     * 引数がそれぞれnot nullであれば、戻り値が帰ってくることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testExecute01() throws Exception {
        // テスト対象のインスタンス化
        CSVFileUpdateDAO fileUpdateDAO = new CSVFileUpdateDAO();

        // 引数の設定
        String fileName = "aaa";
        Class<CSVFileUpdateDAO_Stub01> clazz = CSVFileUpdateDAO_Stub01.class;

        // 前提条件の設定
        HashMap<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        UTUtil.setPrivateField(fileUpdateDAO, "columnFormatterMap",
                columnFormatterMap);

        // テスト実施
        FileLineWriter<CSVFileUpdateDAO_Stub01> fileLineWriter = fileUpdateDAO
                .execute(fileName, clazz);

        // 返却値の確認
        assertEquals(CSVFileLineWriter.class, fileLineWriter.getClass());

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(CSVFileLineWriter.class,
                "<init>"));
        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileUpdateDAO.class,
                "getColumnFormatterMap"));
        List arguments = VMOUTUtil.getArguments(CSVFileLineWriter.class,
                "<init>", 0);
        assertEquals(fileName, arguments.get(0));
        assertEquals(clazz, arguments.get(1));
        assertEquals(columnFormatterMap, arguments.get(2));

        // クローズ処理
        fileLineWriter.closeFile();
        // テスト後ファイルを削除
        File file = new File("aaa");
        file.delete();
    }

}
