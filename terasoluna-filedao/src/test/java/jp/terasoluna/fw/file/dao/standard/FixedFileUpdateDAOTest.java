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
import java.util.Map;

import jp.terasoluna.fw.file.dao.FileLineWriter;
import jp.terasoluna.fw.file.ut.VMOUTUtil;
import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.FixedFileUpdateDAO} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> 固定長ファイル用のFileLineWriterを生成する。<br>
 * AbstractFileUpdateDAOのサブクラス。
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.FixedFileUpdateDAO
 */
public class FixedFileUpdateDAOTest extends TestCase {

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(FixedFileUpdateDAOTest.class);
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
    public FixedFileUpdateDAOTest(String name) {
        super(name);
    }

    /**
     * testExecute01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:not null かつ""(空文字)でない<br>
     * Stringインスタンス<br>
     * "aaa"<br>
     * (引数) clazz: not null(FileFormatアノテーションを持つスタブを使用)<br>
     * (状態) getColumnFormatterMap（）:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"java.lang.String"=NullColumnFormatterインスタンス<br>
     * <br>
     * 期待値：(戻り値) fileLineWriter:FixedFileLineWriterのインスタンス<br>
     * (状態変化) FixedFileLineWriter#FixedFileLineWriter(): 1回呼ばれる<br>
     * 引数を確認する<br>
     * <br>
     * 正常パターン<br>
     * 引数がそれぞれnot nullであれば、戻り値が帰ってくることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testExecute01() throws Exception {
        // テスト対象のインスタンス化
        FixedFileUpdateDAO fileUpdateDAO = new FixedFileUpdateDAO();

        // 引数の設定
        String fileName = "aaa";
        Class<FixedFileUpdateDAO_Stub01> clazz = FixedFileUpdateDAO_Stub01.class;

        // 前提条件の設定
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        UTUtil.setPrivateField(fileUpdateDAO, "columnFormatterMap",
                columnFormatterMap);

        // テスト実施
        FileLineWriter<FixedFileUpdateDAO_Stub01> fileLineWriter = fileUpdateDAO
                .execute(fileName, clazz);

        // 返却値の確認
        assertEquals(FixedFileLineWriter.class.getName(), fileLineWriter
                .getClass().getName());

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(FixedFileLineWriter.class,
                "<init>"));
        List arguments = VMOUTUtil.getArguments(FixedFileLineWriter.class,
                "<init>", 0);
        assertSame(fileName, arguments.get(0));
        assertSame(clazz, arguments.get(1));
        assertSame(columnFormatterMap, arguments.get(2));

        // 後処理
        fileLineWriter.closeFile();
        // テスト後ファイルを削除
        File file = new File("aaa");
        file.delete();
    }

}
