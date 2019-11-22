package com.io2.service01;

object TestGroupBy {
  def main(args: Array[String]): Unit = {
    var user = List("1,张杨,男", "2,曹操,女", "3,刘备,男")
    user.flatMap(_.split(",")).groupBy(a=>a).toList.map(t=>(t._1,t._2.size)).foreach(print)
  }
}
