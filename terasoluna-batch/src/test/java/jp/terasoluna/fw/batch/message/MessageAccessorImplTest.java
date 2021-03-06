/*
 * Copyright (c) 2011 NTT DATA Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.terasoluna.fw.batch.message;

import org.springframework.context.ApplicationContext;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import jp.terasoluna.fw.batch.message.MessageAccessor;
import jp.terasoluna.fw.batch.util.MessageUtil;
import jp.terasoluna.fw.util.PropertyUtil;
import junit.framework.TestCase;

/**
 * 事前条件<br>
 * <br>
 * 
 * ・src/test/resourcesフォルダ配下にAppricationResources.propertiesが存在すること。<br>
 * <br>
 * 
 * ・プロパティMessageAccessor.defaultの値が設定されていること。<br>
 * <fieldset><legend>batch.properties設定例</legend> #メッセージソースアクセサのBean名<br>
 * MessageAccessor.default=msgAcc
 * 
 * </fieldset> <br>
 * ・Bean定義ファイルにプロパティで設定されたの値のBean名が設定されていること。<br>
 * <fieldset><legend>AdminContext.xml設定例</legend>
 * 
 * &lt;!-- メッセージアクセサ --&gt;<br>
 * &lt;bean id=&quot;msgAcc&quot;
 * class=&quot;jp.terasoluna.fw.batch.message.MessageAccessorImpl&quot;
 * /&gt;
 * 
 * </fieldset> <br>
 * ・messages.propertiesファイルが存在すること<br>
 * 
 */
public class MessageAccessorImplTest extends TestCase {

	/**
	 * プロパティ値取得値
	 */
	private String value = null;

	/**
	 * コンテナ用のフィールド
	 */
	private ApplicationContext context;

	/**
	 * MessageAccessorクラスのフィールド
	 */
	private MessageAccessor messageAccessor;

	@Override
	protected void setUp() throws Exception {

		// メッセージソースアクセサのBean名取得
		context = new ClassPathXmlApplicationContext(
				"beansDef/AdminContext.xml");
		value = PropertyUtil.getProperty("messageAccessor.default");
		messageAccessor = (MessageAccessor) context.getBean(value,
				MessageAccessor.class);
	}

	/**
	 * testGetMessage01()<br>
	 * <br>
	 * 事前状態：errors.alphaNumericStringが設定されていること<br>
	 * <br>
	 * テスト概要：パラメータを1つ持つメッセージキーに該当するメッセージを正常に取得することができることを確認する<br>
	 * <br>
	 * 確認項目：プロパティに設定したメッセージが取得されていることを確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testGetMessage01() throws Exception {

		String[] args = { "test1" };

		String result = messageAccessor.getMessage("errors.alphaNumericString",
				args);

		assertEquals("test1には半角英数字で入力してください.", result);
	}

	/**
	 * testGetMessage02()<br>
	 * <br>
	 * 事前状態：errors.alphaNumericStringが設定されていること<br>
	 * <br>
	 * テスト概要：パラメータにnullを設定し、
	 * メッセージキーに該当するメッセージを取得したときパラメータが変換されずにメッセージが返却されることを確認する<br>
	 * <br>
	 * 確認項目：プロパティに設定したメッセージが取得されていることを確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testGetMessage02() throws Exception {

		String[] args = { "test1" };

		String result = messageAccessor.getMessage("errors.alphaNumericString",
				null);

		assertEquals("{0}には半角英数字で入力してください.", result);
	}

	/**
	 * testGetMessage03()<br>
	 * <br>
	 * テスト概要：存在しないメッセージキーの場合、NoSuchMessageExceptionがスローされることを確認する<br>
	 * <br>
	 * 確認項目：NoSuchMessageExceptionがスローされていることを確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testGetMessage03() throws Exception {

		try {
			String result = messageAccessor.getMessage("test", null);
			fail();
		} catch (NoSuchMessageException e) {
			// 何もしない
		}

	}

	/**
	 * testGetMessage03()<br>
	 * <br>
	 * テスト概要：メッセージキーに空文字を設定した場合、NoSuchMessageExceptionがスローされることを確認する<br>
	 * <br>
	 * 確認項目：NoSuchMessageExceptionがスローされていることを確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testGetMessage04() throws Exception {

		try {
			String result = messageAccessor.getMessage("", null);
			fail();
		} catch (NoSuchMessageException e) {
			// 何もしない
		}
	}

	/**
	 * testGetMessage03()<br>
	 * <br>
	 * テスト概要：メッセージキーにnullを設定した場合、NoSuchMessageExceptionがスローされることを確認する<br>
	 * <br>
	 * 確認項目：NoSuchMessageExceptionがスローされていることを確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testGetMessage05() throws Exception {

		try {
			String result = messageAccessor.getMessage(null, null);
			fail();
		} catch (NoSuchMessageException e) {
			// 何もしない
		}
	}

}
