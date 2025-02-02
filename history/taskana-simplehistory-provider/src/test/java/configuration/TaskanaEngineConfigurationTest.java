package configuration;

import static org.assertj.core.api.Assertions.assertThat;

import acceptance.AbstractAccTest;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;

import pro.taskana.TaskanaEngineConfiguration;
import pro.taskana.common.api.TaskanaEngine;
import pro.taskana.common.test.config.DataSourceGenerator;

/** Unit Test for TaskanaEngineConfigurationTest. */
class TaskanaEngineConfigurationTest extends AbstractAccTest {

  @Test
  void testCreateTaskanaEngine() throws Exception {
    DataSource ds = DataSourceGenerator.getDataSource();
    TaskanaEngineConfiguration taskEngineConfiguration =
        new TaskanaEngineConfiguration(ds, false, false, DataSourceGenerator.getSchemaName());

    TaskanaEngine te = taskEngineConfiguration.buildTaskanaEngine();

    assertThat(te).isNotNull();
  }

  @Test
  void testCreateTaskanaHistoryEventWithNonDefaultSchemaName() throws Exception {
    resetDb("SOMECUSTOMSCHEMANAME");
    long count = getHistoryService().createTaskHistoryQuery().workbasketKeyIn("wbKey1").count();
    assertThat(count).isZero();
    getHistoryService()
        .create(
            AbstractAccTest.createTaskHistoryEvent(
                "wbKey1", "taskId1", "type1", "Some comment", "wbKey2", "someUserId"));
    count = getHistoryService().createTaskHistoryQuery().workbasketKeyIn("wbKey1").count();
    assertThat(count).isOne();
  }
}
