package com.hannesdorfmann.adapterdelegates4.sample.animations;

/**
 * @author Hannes Dorfmann
 */
public class Item {
  public int id;
  public String text;
  public int color;

  public Item(int id, String text, int color) {
    this.id = id;
    this.text = text;
    this.color = color;
  }

  public Item copy() {
    return new Item(id, text, color);
  }
}
