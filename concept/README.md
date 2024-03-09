### 接口说明  


- [Field](src/main/java/com/github/mbmll/concept/fields/Field.java) 提供公共字段的接口,方便函数式同用方法构建
  - [CreateTime](src%2Fmain%2Fjava%2Fcom%2Fgithub%2Fmbmll%2Fconcept%2Ffields%2FCreateTime.java) createTime
  - [UpdateTime](src%2Fmain%2Fjava%2Fcom%2Fgithub%2Fmbmll%2Fconcept%2Ffields%2FUpdateTime.java) updateTime
  - [Time](src%2Fmain%2Fjava%2Fcom%2Fgithub%2Fmbmll%2Fconcept%2Ffields%2FTime.java) 组合 [CreateTime](src%2Fmain%2Fjava%2Fcom%2Fgithub%2Fmbmll%2Fconcept%2Ffields%2FCreateTime.java) 和 
[UpdateTime](src%2Fmain%2Fjava%2Fcom%2Fgithub%2Fmbmll%2Fconcept%2Ffields%2FUpdateTime.java) 
- [Mapper](src%2Fmain%2Fjava%2Fcom%2Fgithub%2Fmbmll%2Fconcept%2FMapper.java)  再次看到这个类, 觉得不是好的设计. 面对复杂转换的时候 
  List\<Convertors> 似乎更好. 或许可以用 Mapper 包裹 List\<Convertors>, 既满足框架需要, 又可以灵活定制.
- [ThrowableConsumer](src%2Fmain%2Fjava%2Fcom%2Fgithub%2Fmbmll%2Fconcept%2FThrowableConsumer.java) 这是个很实用的类. 很多时候,
  我们需要抛出异常