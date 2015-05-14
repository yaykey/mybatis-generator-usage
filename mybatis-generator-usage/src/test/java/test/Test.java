//package test;
//
//import java.io.Reader;
//import java.util.List;
//
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//
//import com.feinno.fdc.op.framework.module.Myuser;
//import com.feinno.fdc.op.framework.module.MyuserExample;
//import com.feinno.fdc.op.framework.persistence.mapper.MyuserMapper;
//
//
//public class Test {
//
//	public static void main(String[] args) throws Exception {
//		String resource = "MapperConfig.xml";
//		Reader reader = Resources.getResourceAsReader(resource);
//		SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder()
//				.build(reader);
//		SqlSession sqlSession = sqlMapper.openSession();
//
//		MyuserExample myuserExample = new MyuserExample();
//		myuserExample.or().andIdIsNotNull();
//
//		MyuserMapper mapper = sqlSession.getMapper(MyuserMapper.class);
//
//		List<Myuser> allRecords = mapper.selectByExample(myuserExample);
//
//		try {
//			for (Myuser u : allRecords) {
//				System.out.println(u);
//			}
//		} finally {
//			sqlSession.close();
//		}
//
//		// PetExample pet = new PetExample();
//		// pet.or().andDeathIsNotNull();
//		// try {
//		// PetMapper mapper = sqlSession.getMapper(PetMapper.class);
//		// List<Pet> allRecords = mapper.selectByExample(pet);
//		// for (Pet s : allRecords)
//		// System.out.println(s);
//		// } finally {
//		// sqlSession.close();
//		// }
//	}
//}
