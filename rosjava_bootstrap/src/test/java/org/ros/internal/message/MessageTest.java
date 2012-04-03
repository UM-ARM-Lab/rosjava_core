/*
 * Copyright (C) 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ros.internal.message;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.ros.internal.message.topic.TopicDefinitionResourceProvider;
import org.ros.message.MessageFactory;

import java.util.ArrayList;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MessageTest {

  private TopicDefinitionResourceProvider topicDefinitionResourceProvider;
  private MessageFactory messageFactory;

  @Before
  public void setUp() {
    topicDefinitionResourceProvider = new TopicDefinitionResourceProvider();
    messageFactory = new DefaultMessageFactory(topicDefinitionResourceProvider);
  }

  @Test
  public void testCreateEmptyMessage() {
    topicDefinitionResourceProvider.add("foo/foo", "");
    messageFactory.newFromType("foo/foo");
  }

  @Test
  public void testCreateEmptyMessageWithBlankLines() {
    topicDefinitionResourceProvider.add("foo/foo", "\n\n\n\n\n");
    messageFactory.newFromType("foo/foo");
  }

  @Test
  public void testString() {
    String data = "Hello, ROS!";
    Message message = messageFactory.newFromType("std_msgs/String");
    message.setString("data", data);
    assertEquals(data, message.getString("data"));
  }

  @Test
  public void testStringWithComments() {
    topicDefinitionResourceProvider.add("foo/foo",
        "# foo\nstring data\n    # string other data");
    String data = "Hello, ROS!";
    Message message = messageFactory.newFromType("foo/foo");
    message.setString("data", data);
    assertEquals(data, message.getString("data"));
  }

  @Test
  public void testInt8() {
    byte data = 42;
    Message message = messageFactory.newFromType("std_msgs/Int8");
    message.setInt8("data", data);
    assertEquals(data, message.getInt8("data"));
  }

  @Test
  public void testNestedMessage() {
    topicDefinitionResourceProvider.add("foo/foo", "bar data");
    topicDefinitionResourceProvider.add("foo/bar", "int8 data");
    Message fooMessage = messageFactory.newFromType("foo/foo");
    Message barMessage = messageFactory.newFromType("foo/bar");
    fooMessage.setMessage("data", barMessage);
    byte data = 42;
    barMessage.setInt8("data", data);
    assertEquals(data, fooMessage.getMessage("data").getInt8("data"));
  }

  @Test
  public void testConstantInt8() {
    topicDefinitionResourceProvider.add("foo/foo", "int8 data=42");
    Message message = messageFactory.newFromType("foo/foo");
    assertEquals(42, message.getInt8("data"));
  }

  @Test
  public void testConstantString() {
    topicDefinitionResourceProvider.add("foo/foo", "string data=Hello, ROS! # comment ");
    Message message = messageFactory.newFromType("foo/foo");
    assertEquals("Hello, ROS! # comment", message.getString("data"));
  }

  public void testInt8List() {
    topicDefinitionResourceProvider.add("foo/foo", "int8[] data");
    Message message = messageFactory.newFromType("foo/foo");
    ArrayList<Byte> data = Lists.newArrayList((byte) 1, (byte) 2, (byte) 3);
    message.setInt8List("data", data);
    assertEquals(data, message.getInt8List("data"));
  }
}