package com.hannesdorfmann.adapterdelegates3;

import android.support.v7.recyclerview.extensions.AsyncListDiffer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by six_hundreds on 28.06.18.
 */
@RunWith(RobolectricTestRunner.class)
public class DiffDelegationAdapterTest {

    @Test
    public void checkDelegatesManagerNotNull() {
        new DiffDelegationAdapter() {
            @Override
            public int getItemCount() {
                Assert.assertNotNull(delegatesManager);
                return 0;
            }
        };

    }

    @Test
    public void checkDiffCallbackNotNull() {
        new DiffDelegationAdapter() {
            @Override
            public int getItemCount() {
                Assert.assertNotNull(differ);
                return 0;
            }
        };
    }

    @Test
    public void checkDiffCallbackInstance() {
        final DiffCallbackImpl<DiffItem> callback = new DiffCallbackImpl<>();
        new DiffDelegationAdapter<DiffItem>(callback) {
            @Override
            public int getItemCount() {
                AsyncListDiffer asyncListDiffer = new AsyncListDiffer<>(this, callback);
                Assert.assertEquals(differ, asyncListDiffer);
                return 0;
            }
        };
    }

    @Test
    public void checkSubmittedItems() {
        DiffDelegationAdapter<TestItem> adapter = new DiffDelegationAdapter<>();
        List<TestItem> items = new ArrayList<TestItem>() {{
            add(new TestItem(0, "Content0"));
            add(new TestItem(1, "Content1"));
            add(new TestItem(2, "Content2"));
            add(new TestItem(3, "Content3"));
            add(new TestItem(4, "Content4"));
        }};
        adapter.setItems(items);
        Assert.assertEquals(adapter.getItems(), items);
    }

    @Test
    public void checkUpdatedItems() {
        DiffDelegationAdapter<TestItem> adapter = new DiffDelegationAdapter<>();
        List<TestItem> items = new ArrayList<TestItem>() {{
            add(new TestItem(0, "Content0"));
            add(new TestItem(1, "Content1"));
            add(new TestItem(2, "Content2"));
            add(new TestItem(3, "Content3"));
            add(new TestItem(4, "Content4"));
        }};
        adapter.setItems(items);

        List<TestItem> updatedItems = new ArrayList<TestItem>(items) {{
            set(0, new TestItem(get(0).getId(), get(0).getContent() + " Updated"));
            set(3, new TestItem(get(3).getId(), get(3).getContent() + " Updated"));
        }};

        adapter.setItems(updatedItems);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TestItem updated0 = adapter.getItems().get(0);
        TestItem updated3 = adapter.getItems().get(3);

        Assert.assertTrue(updated0.getContent().contains("Updated"));
        Assert.assertTrue(updated3.getContent().contains("Updated"));
        Assert.assertTrue(adapter.getItems().size() == items.size());
    }

    @Test
    public void checkDeletedItems() {
        final DiffDelegationAdapter<TestItem> adapter = new DiffDelegationAdapter<>();
        List<TestItem> items = new ArrayList<TestItem>() {{
            add(new TestItem(0, "Content0"));
            add(new TestItem(1, "Content1"));
            add(new TestItem(2, "Content2"));
            add(new TestItem(3, "Content3"));
            add(new TestItem(4, "Content4"));
        }};
        adapter.setItems(items);

        List<TestItem> updatedItems = new ArrayList<>(items);
        updatedItems.remove(0);
        updatedItems.remove(3);

        items = updatedItems;

        adapter.setItems(items);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Integer> expectedList = new ArrayList<Integer>() {
            {
                add(1);
                add(2);
                add(4);
            }
        };

        List<Integer> actualList = new ArrayList<>();

        for (TestItem item : adapter.getItems()) {
            actualList.add(item.getId());
        }
        Assert.assertTrue(expectedList.containsAll(actualList) && actualList.containsAll(expectedList));
    }


    private static class TestItem implements DiffItem {

        private int id;

        private String content;

        public TestItem(int id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public long getItemId() {
            return id;
        }

        @Override
        public int getItemHash() {
            return hashCode();
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestItem testItem = (TestItem) o;

            if (id != testItem.id) return false;
            return content != null ? content.equals(testItem.content) : testItem.content == null;
        }

        @Override
        public int hashCode() {
            int result = id;
            result = 31 * result + (content != null ? content.hashCode() : 0);
            return result;
        }

        public int getId() {
            return id;
        }

        public String getContent() {
            return content;
        }
    }
}
