import { cookies } from "next/headers";
import { redirect } from "next/navigation";

export default async function Home() {
  const session = (await cookies()).get("SESSION");
  if (!session) redirect("/login");
  return <main>대시보드</main>;
}